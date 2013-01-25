/**
 * Copyright (c) 2013, Institute of Information Systems (Sven Groppe), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package lupos.datastructures.patriciatrie;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import lupos.datastructures.items.Triple;
import lupos.datastructures.items.literal.Literal;
import lupos.datastructures.parallel.BoundedBuffer;
import lupos.datastructures.patriciatrie.TrieBag;
import lupos.datastructures.patriciatrie.diskseq.DBSeqTrieBag;
import lupos.datastructures.patriciatrie.exception.TrieNotCopyableException;
import lupos.datastructures.patriciatrie.exception.TrieNotMergeableException;
import lupos.datastructures.patriciatrie.ram.RBTrieBag;
import lupos.engine.evaluators.CommonCoreQueryEvaluator;
import lupos.engine.operators.tripleoperator.TripleConsumer;
import lupos.misc.TimeInterval;

/**
 * Class for sorting the RDF terms of RDF data using trie bags.
 * This sorting algorithm is optimized for sorting in computers with large main memories.
 * Initial runs (up to a certain size) are generated by threads in main memory.
 * The initial runs are merged in main memory (in an own thread) and only intermediately stored on disk if there is no free main memory any more.
 * Finally all remaining runs are merged into one run...  
 */
public class ExternalParallelTrieSort {
	
	// garbage collection cycles are once per minute per default, with
	// java -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 ...
	// it can be set to whatever you like (here 1 hour)...
	
	/**
	 * Main method to start sorting the RDF terms of RDF data using trie bags
	 * @param args command line arguments
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Sorting RDF terms of large RDF data in computers with large main memory...");
		System.out.println("Call:");
		System.out.println("java lupos.datastructures.patriciatrie.ExternalParallelTrieSort DATAFILE FORMAT [NUMBER_INITIAL_RUN_GENERATION_THREADS NUMBER_ELEMENTS_IN_INITIAL_RUNS NUMBER_OF_RUNS_TO_JOIN FREE_MEMORY_LIMIT]");
		System.out.println("Example:");
		System.out.println("java -server -XX:+UseParallelGC -XX:+AggressiveOpts -Xms60G -Xmx60G lupos.test.ExternalParallelMergeSort SomeFiles.txt MULTIPLEN3 8 1000 2 100000");
		if(!(args.length==2 || args.length==6)){
			System.err.println("Wrong number of arguments!");
			return;
		}
		Date start = new Date();
		System.out.println("\nStart processing:"+start+"\n");		
		ExternalParallelTrieSort sorter = (args.length==2)?
				new ExternalParallelTrieSort():
				new ExternalParallelTrieSort(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Long.parseLong(args[5]));
				
		System.out.println("\nParameters:\n" + sorter.parametersToString() + "\n");
		sorter.sort(new BufferedInputStream(new FileInputStream(args[0])), args[1]);
		Date end = new Date();
		System.out.println("\nEnd processing:"+end);		
		System.out.println("\nDuration:   " + (end.getTime() - start.getTime())/1000.0 + " seconds\n          = "+new TimeInterval(start, end));
		
		System.out.println("\nNumber of waits of initial run generators:" + sorter.getNumberOfWaitsOfInitialRunGenerator());
		System.out.println("Number of waits of merger thread:" + sorter.getNumberOfWaitsOfMerger());
	}
	
	/**
	 * Constructor to set a lot of parameters
	 * @param NUMBER_INITIAL_RUN_GENERATION_THREADS
	 * @param NUMBER_ELEMENTS_IN_INITIAL_RUNS
	 * @param NUMBER_OF_RUNS_TO_JOIN
	 * @param FREE_MEMORY_LIMIT
	 */
	public ExternalParallelTrieSort(final int NUMBER_INITIAL_RUN_GENERATION_THREADS, final int NUMBER_ELEMENTS_IN_INITIAL_RUNS, final int NUMBER_OF_RUNS_TO_JOIN, final long FREE_MEMORY_LIMIT){
		this.NUMBER_INITIAL_RUN_GENERATION_THREADS = NUMBER_INITIAL_RUN_GENERATION_THREADS;
		this.NUMBER_ELEMENTS_IN_INITIAL_RUNS = NUMBER_ELEMENTS_IN_INITIAL_RUNS;
		this.NUMBER_OF_RUNS_TO_JOIN = NUMBER_OF_RUNS_TO_JOIN;
		this.FREE_MEMORY_LIMIT = FREE_MEMORY_LIMIT;
	}
	
	/**
	 * Default constructor using default parameters...
	 */
	public ExternalParallelTrieSort(){
		this(8, 1000, 2, 100000);
	}
	
	private final int NUMBER_INITIAL_RUN_GENERATION_THREADS;
	
	private final int NUMBER_ELEMENTS_IN_INITIAL_RUNS;
	
	private final int NUMBER_OF_RUNS_TO_JOIN;
	
	private final long FREE_MEMORY_LIMIT;

	private final LinkedList<TrieBag> triesOnDisk = new LinkedList<TrieBag>();
	
	private int numberOfWaitsOfInitialRunGenerator = 0;
	
	private int numberOfWaitsOfMerger = 0;
	
	/**
	 * an id for generating unique file names for the tries stored on disk...
	 */
	private static int trieID = 0;
	
	/**
	 * @return the file name for a new trie stored on disk
	 */
	public static String getFilenameForNewTrie(){
		return "Trie_"+(ExternalParallelTrieSort.trieID++);
	}
	
	/**
	 * sorting algorithm
	 * @param dataFiles the data
	 * @param format the format of the data
	 * @throws Exception if something went wrong
	 */
	public void sort(final InputStream dataFiles, String format) throws Exception {
		final BoundedBuffer<String> buffer = new BoundedBuffer<String>();
		
		// initialize threads for generating initial runs
		final BoundedBuffer<TrieBag> initialRunsLevel0 = new BoundedBuffer<TrieBag>(this.NUMBER_INITIAL_RUN_GENERATION_THREADS*3);
		
		InitialRunGenerator[] initialRunGenerationThreads = new InitialRunGenerator[this.NUMBER_INITIAL_RUN_GENERATION_THREADS];
		
		for(int i=0; i<this.NUMBER_INITIAL_RUN_GENERATION_THREADS; i++){
			initialRunGenerationThreads[i] = new InitialRunGenerator(buffer, initialRunsLevel0);
			initialRunGenerationThreads[i].start();
		}
		
		// start the merge thread...
		final ArrayList<LinkedList<TrieBag>> levels = new ArrayList<LinkedList<TrieBag>>();
		Merger merger = new Merger(initialRunsLevel0, levels);
		merger.start();
		
		// now parse the data...
		CommonCoreQueryEvaluator.readTriples(format, dataFiles, 
				new TripleConsumer(){
					@Override
					public void consume(Triple triple) {
						for(Literal literal: triple){
							try {
								buffer.put(literal.originalString());
							} catch (InterruptedException e) {
								System.err.println(e);
								e.printStackTrace();
							}
						}
					}
		});
		
		// signal that the all data is parsed (and nothing will be put into the buffer any more) 
		buffer.endOfData();
		
		// wait for threads to finish generating initial runs...  
		for(int i=0; i<this.NUMBER_INITIAL_RUN_GENERATION_THREADS; i++){
			try {
				initialRunGenerationThreads[i].join();
			} catch (InterruptedException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}
		
		// start remaining merging phase...
		// signal no initial run will be generated any more
		initialRunsLevel0.endOfData();
		
		// wait for merger thread!
		try {
			merger.join();
		} catch (InterruptedException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
		// final merge phase: merge all tries (which are still in memory or stored on disk)
		// first collect all remaining tries!
		LinkedList<TrieBag> triestoBeFinallyMerged = new LinkedList<TrieBag>(this.triesOnDisk);
		for(LinkedList<TrieBag> level: levels){
			triestoBeFinallyMerged.addAll(level);
		}
		TrieBag result;
		if(triestoBeFinallyMerged.size()==0){
			System.err.println("No tries there to be merged!");
			return;
		} else if(triestoBeFinallyMerged.size()==1){
			// already merge in main memory?
			result = triestoBeFinallyMerged.get(0);
		} else {
			result = new DBSeqTrieBag(getFilenameForNewTrie());
			result.merge(triestoBeFinallyMerged);
		}
		
		// just access all elements in the bag by iterating one time through
		Iterator<String> it = result.keyIterator();
		int i=0;
		while(it.hasNext()){
			it.next();
			i++;
			// System.out.println((++i)+":"+it.next());
		}
		System.out.println("Number of sorted RDF terms:"+i);
		System.out.println("There should be " + (i/3) + " triples read!");
	}
	
	/**
	 * @return a string describing the current parameters of this object
	 */
	public String parametersToString(){
		return 
			"NUMBER_INITIAL_RUN_GENERATION_THREADS:" + this.NUMBER_INITIAL_RUN_GENERATION_THREADS +			
			"\nNUMBER_ELEMENTS_IN_INITIAL_RUNS      :" + this.NUMBER_ELEMENTS_IN_INITIAL_RUNS +			
			"\nNUMBER_OF_RUNS_TO_JOIN               :" + this.NUMBER_OF_RUNS_TO_JOIN +			
			"\nFREE_MEMORY_LIMIT                    :" + this.FREE_MEMORY_LIMIT;
	}
	
	public int getNumberOfWaitsOfInitialRunGenerator() {
		return this.numberOfWaitsOfInitialRunGenerator;
	}

	public int getNumberOfWaitsOfMerger() {
		return this.numberOfWaitsOfMerger;
	}

	/**
	 * Just to make it exchangeable
	 * @return a new trie bag
	 */
	public static TrieBag generateTrie(){
		return new RBTrieBag();
	}
	
	/**
	 * Thread for generating the initial runs... 
	 */
	public class InitialRunGenerator extends Thread {
		
		private final BoundedBuffer<String> buffer;
		private TrieBag trie;
		private final BoundedBuffer<TrieBag> initialRunsLevel0;
		
		public InitialRunGenerator(final BoundedBuffer<String> buffer, final BoundedBuffer<TrieBag> initialRunsLevel0){
			this.buffer = buffer;
			this.trie = generateTrie();
			this.initialRunsLevel0 = initialRunsLevel0;
		}
		
		@Override
		public void run(){
			try {
				int numberInTrie = 0;
				while(true) {
					String item = this.buffer.get();
					if(item==null){
						break;
					}
					if(this.trie.add(item)){
						numberInTrie++;
					}
					if(numberInTrie > ExternalParallelTrieSort.this.NUMBER_ELEMENTS_IN_INITIAL_RUNS){
						// this run exceeds the limit for number of elements and becomes therefore a new initial run
						this.finishInitialRun();
						numberInTrie =0;
					}
				}
				// the (not full) trie becomes an initial run because all RDF terms have been consumed
				this.finishInitialRun();
			} catch (InterruptedException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}
		
		public void finishInitialRun() throws InterruptedException{
			if(this.trie.size()>0){
				if(this.initialRunsLevel0.isCurrentlyFull()){
					ExternalParallelTrieSort.this.numberOfWaitsOfInitialRunGenerator++;
				}
				this.initialRunsLevel0.put(this.trie);
				this.trie = generateTrie();
			}
		}
	}
	
	public class Merger extends Thread {
		
		private final BoundedBuffer<TrieBag> initialRunsLevel0;
		private final ArrayList<LinkedList<TrieBag>> levels;
		
		public Merger(final BoundedBuffer<TrieBag> initialRunsLevel0, final ArrayList<LinkedList<TrieBag>> levels){
			this.initialRunsLevel0 = initialRunsLevel0; 
			this.levels = levels;
		}
		
		@Override
		public void run(){

			try {
				while(true){
					// get as many tries to merge as specified
					if(this.initialRunsLevel0.size()<ExternalParallelTrieSort.this.NUMBER_OF_RUNS_TO_JOIN){
						ExternalParallelTrieSort.this.numberOfWaitsOfMerger++;
					}
					Object[] bagsToMerge = this.initialRunsLevel0.get(ExternalParallelTrieSort.this.NUMBER_OF_RUNS_TO_JOIN, ExternalParallelTrieSort.this.NUMBER_OF_RUNS_TO_JOIN);
					if(bagsToMerge!=null && bagsToMerge.length>0){
						TrieBag trie = null;
						if(bagsToMerge.length>1){
							trie = generateTrie();
							LinkedList<TrieBag> toBeMerged = new LinkedList<TrieBag>();
							for(Object bag: bagsToMerge){
								toBeMerged.add((TrieBag) bag);
							}
							trie.merge(toBeMerged);
						} else if(bagsToMerge.length==1) {
							trie = (TrieBag) bagsToMerge[0];
						}
						this.addToLevel(0, trie);
					} else {
						// no new initial runs any more => merge thread finishes
						break;
					}
				}
			} catch (InterruptedException e) {
				System.err.println(e);
				e.printStackTrace();
			} catch (TrieNotMergeableException e) {
				System.err.println(e);
				e.printStackTrace();
			} catch (TrieNotCopyableException e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}
		
		/**
		 * This method adds merged tries (at a certain level) and merges the tries at this level if possible.
		 * Using levels has the effect that only ties of similar sizes are merged (and not large ones with small ones, which is not so fast (? to be checked!) because of an asymmetric situation)
		 * @param addLevel the level to which the merged trie is added
		 * @param toBeAdded the merged trie to be added
		 * @throws TrieNotMergeableException
		 * @throws TrieNotCopyableException
		 */
		public void addToLevel(final int addLevel, final TrieBag toBeAdded) throws TrieNotMergeableException, TrieNotCopyableException{			
			while(this.levels.size()<=addLevel){
				this.levels.add(new LinkedList<TrieBag>());
			}
			LinkedList<TrieBag> level = this.levels.get(addLevel);
			level.add(toBeAdded);
			while(level.size()>=ExternalParallelTrieSort.this.NUMBER_OF_RUNS_TO_JOIN){
				// we should merge the tries at this level
				
				ArrayList<TrieBag> listOfTriesToBeMerged = new ArrayList<TrieBag>(ExternalParallelTrieSort.this.NUMBER_OF_RUNS_TO_JOIN);
				
				while(listOfTriesToBeMerged.size()<ExternalParallelTrieSort.this.NUMBER_OF_RUNS_TO_JOIN){
					listOfTriesToBeMerged.add(level.remove());					
				}
				
				TrieBag resultOfMerge = generateTrie();
				resultOfMerge.merge(listOfTriesToBeMerged);
				
				// put the merged trie to one level higher (and recursively merge there the tries if enough are there)
				this.addToLevel(addLevel + 1, resultOfMerge);				
			}
			// is there still enough memory available?
			if(Runtime.getRuntime().freeMemory()<ExternalParallelTrieSort.this.FREE_MEMORY_LIMIT){
				// get one of the biggest tries for storing it on disk and free it in memory...
				int levelnr=this.levels.size();
				do {
					levelnr--;
				} while(levelnr>0 && this.levels.get(levelnr).size()==0);
				if(levelnr==0){
					System.err.println("ExternalParallelMergeSort: Heap space to low or FREE_MEMORY_LIMIT to high...");
					return;
				}
				LinkedList<TrieBag> lastLevel = this.levels.get(levelnr);
				TrieBag trieBag = lastLevel.remove();
				// free memory by storing a trie on disk...
				TrieBag diskbasedTrie = new DBSeqTrieBag(getFilenameForNewTrie());
				diskbasedTrie.copy(trieBag);
				trieBag = null; // just to ensure the garbage collector can do something when called...
				System.gc();
				ExternalParallelTrieSort.this.triesOnDisk.add(diskbasedTrie);
			}
		}
	}
}