

import java.util.LinkedList;
import java.util.Queue;

public class Scheduler 
{
	Queue<Process> readyQ= new LinkedList<Process>();
	Queue<Process> BlockQ= new LinkedList<Process>();
	
	Queue<Process> Q1= new LinkedList<Process>();
	Queue<Process> Q2= new LinkedList<Process>();
	Queue<Process> Q3= new LinkedList<Process>();
	Queue<Process> Q4= new LinkedList<Process>();
	
	boolean end =true;
	boolean Switch=false;
	
	int turn[]={0,15,25,37,40};
	int turnnum[]={0,5,8,6,10};
	Process terminate=null;
	int time=0;
	
	public void setup(Process i)
	{
		readyQ.add(i);	
	}
	public void dispatch()
	{
		while(!readyQ.isEmpty())
		{
			Process p =readyQ.poll();
			Q1.add(p);
		}
		if(Q1.isEmpty()||(Q1.peek().polltime<time))
		{
			if(Q2.isEmpty()||(Q2.peek().polltime<time))
			{
				if(Q3.isEmpty()||(Q3.peek().polltime<time))
				{
					if(Q4.isEmpty()||Q4.peek().polltime<time)
					{
						return;
					}
					else
					{
						while(Q4.peek().elapsetime<turn[4])
						{	
							Q4.peek().Run();
							time+=turn[4];
							if(Q4.peek().abnormalTermination)
							{
								terminate=Q4.poll();
								return;
							}
							if(Q4.peek().polling)
							{
								Q4.peek().polltime=time+40;
								Q4.peek().polling=false;
								Q4.peek().subqueue=1;
								Q1.add(Q4.poll());	
							}
						}
						if(turnnum[Q4.peek().subqueue]==Q4.peek().turns)
						{
							if(Q4.peek().abnormalTermination)
							{
								terminate=Q4.poll();
								return;
							}
							Q3.add(Q4.poll());
						}
						else
						{
							Q4.peek().turns++;
						}
					}
				}
				else
				{
					while(Q3.peek().elapsetime<turn[3])
					{	
						Q3.peek().Run();
						time+=turn[3];
						if(Q3.peek().abnormalTermination)
						{
							terminate=Q3.poll();
							return;
						}
						if(Q3.peek().polling)
						{
							Q3.peek().polltime=time+40;
							Q3.peek().polling=false;
							Q1.add(Q4.poll());	
						}
					}
					if(turnnum[Q3.peek().subqueue]==Q3.peek().turns)
					{
						if(Q3.peek().abnormalTermination)
						{
							terminate=Q3.poll();
							return;
						}
						Q4.add(Q3.poll());	
					}
					else
					{
						Q3.peek().turns++;
					}
				}
			}
			else
			{
				while(Q2.peek().elapsetime<turn[2])
				{	
					Q2.peek().Run();
					if(Q2.peek().abnormalTermination)
					{
						terminate=Q2.poll();
						return;
					}
				}
				if(turnnum[Q2.peek().subqueue]==Q2.peek().turns)
				{
					if(Q2.peek().abnormalTermination)
					{
						terminate=Q2.poll();
						return;
					}
					Q3.add(Q2.poll());
				}
				else
				{
					Q2.peek().turns++;
				}
			}
		}
		else
		{
			while(Q1.peek().elapsetime<turn[4])
			{	
				Q1.peek().Run();
				if(Q1.peek().abnormalTermination)
				{
					terminate=Q1.poll();
					return;
				}
			}
			if(turnnum[Q1.peek().subqueue]==Q1.peek().turns)
			{
				if(Q1.peek().abnormalTermination)
				{
					terminate=Q1.poll();
					return;
				}
				Q2.add(Q1.poll());
			}
			else
			{
				Q1.peek().turns++;
			}
		}
	}
}
	