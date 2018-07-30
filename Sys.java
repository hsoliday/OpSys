

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Sys
{	
	public static void main(String[] args) throws IOException// this program is for an assignment, so it should never be run out
	{														 //side of an environment where this exception will not be thrown
		Queue<Process> arrivalQ=new LinkedList<Process>();//sets up an input to load the processes
			
		String memory="";
		String jobstats="";
		File file = new File("testFile1.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fw);// the above lines are for writing out the stats of the operating system execution 
		
		Scanner scan = new Scanner(new FileReader("arrivals.txt"));
		int job=1;// this is set up too start loading up the jobs for execution
		
		while(scan.hasNext())
		{
			String str= scan.nextLine();
			str=str.substring(4);//scans
			
			int hold = parseNextInt(str);
			String[] processInstructions=dataplex(job);
			
			int[] initialPages={0};
			if(hold!=0)
			{
				Process P= new Process(job,initialPages,processInstructions,(hold/256+1)/4);
				arrivalQ.add(P);
				job++;
			}
		}
		MemManager mem=new MemManager();
		mem.Init(128);
		
		Scheduler scheduler = new Scheduler();
		
		int inc=0;
		scheduler.time=0;
		while(!arrivalQ.isEmpty())
		{
			while(!arrivalQ.isEmpty()&&(mem.acquire(arrivalQ.peek().Maxmemory)[0]!=-1))
			{
				scheduler.setup(arrivalQ.poll());
				System.out.println("Loaded Job");
			}
			scheduler.dispatch();
			if(scheduler.terminate!=null)
			{
				if(scheduler.terminate.pid!=0)
				{
					scheduler.terminate.outTime=scheduler.time;
					scheduler.terminate.Poll=(scheduler.terminate.outTime-scheduler.terminate.inTime)-scheduler.terminate.executiontime;
					jobstats+="PID: "+scheduler.terminate.pid+" Memory: "+scheduler.terminate.memory+" input time: "+scheduler.terminate.inTime+" output time: "+scheduler.terminate.outTime +" execution time: " + scheduler.terminate.executiontime+" downtime:  "+(scheduler.terminate.outTime-scheduler.terminate.inTime-scheduler.terminate.executiontime)+"\r\n";
				}
				
				mem.release(scheduler.terminate.memory);
				
				if(scheduler.terminate.pid==0)
				{
					mem.acquire(0);
				}
				
				scheduler.terminate=null;
			}
			if(scheduler.time/100>inc)
			{
				memory+="Time: "+scheduler.time+" "+ mem.stats()+"\r\n";
				inc++;
			}
			
		}
		
		writer.write(memory);
		writer.write("\n\n\n\n");
		writer.write(jobstats);
		
		scan.close();
		writer.close();
	}
	public static String[] dataplex(int i) throws FileNotFoundException
	{
		String str="jb";
		if(i<10)
		{
			str+='0';
		}
		str+=i;
		str+=".txt";
		
		Scanner tann = new Scanner(new FileReader(str));
		ArrayList<String> arr=new ArrayList<String>();
		
		while(tann.hasNext())
		{
			arr.add(tann.nextLine());
		}
		String[] strarr=new String[arr.size()];
		
		for(int k=0;k<arr.size()-1;k++)
		{
			strarr[k]=arr.get(k);
		}
		tann.close();
		return strarr;
	}
	public static int parseNextInt(String str)
	{
		String hold="";
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)==' ')
			{
				break;
			}
			else
			{
				hold=hold+str.charAt(i);
			}
		}
	return Integer.parseInt(hold);
	}
}

