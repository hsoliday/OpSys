

public class MemManager 
{
	private int usedmemory;
	private int max;
	private int numjobs;
	boolean[] MMT;
	// virtual memory allocation is going to give 1/4 pages
	// mmt page table
	public void Init(int i)
	{
		usedmemory=0;
		max=i;
		MMT=new boolean[i];
		for(int j=0;j<i-1;j++)
		{
			MMT[j]=false;
		}
	}
	public int[] acquire(int i)
	{
		if(i<(max-usedmemory))
		{
			usedmemory+=i;
			numjobs++;
			return giveMem(i);
		}
		else
		{
			int[]temp={-1};
			return temp;
		}
	}
	public void release(Page[] memory)
	{
		usedmemory-=memory.length;
		numjobs--;
		for(int i=0;i<memory.length-1;i++)
		{
			MMT[memory[i].location]=false;
		}
	}
	public String stats()
	{
		return "number of jobs: "+numjobs+" average job size: "+((double)usedmemory/(double)numjobs)+" total free space: "+(max-usedmemory);
	}
	private int[]giveMem(int i)
	{
		int[] v=new int[i];
		for(int j=0;j<v.length-1;j++)
		{
			for(int k=0;k<MMT.length-1;k++)
			{
				if(!MMT[k])
					{
						v[j]=k;
						MMT[k]=true;
						break;
					}
			}
		}
		return v;
	}

}
