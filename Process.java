


public class Process {
	int inTime=0;
	int outTime=0;
	int Poll=0;
	int executiontime=0;
	int subqueue;
	int turns;
	boolean polling;
	int polltime;
	final int COM_RUN_TIME=2;
	final int PAGE_GET_TIME=15;
	// things neccessary for operation
	boolean abnormalTermination;
	int elapsetime;
	int loadedPages;
	int[] allotedPageLocations;
	String[] Commands;
	int instructionPointer;
	Page[] memory;
	int Maxmemory;
	int pid;
	public Process(int PID,int[] pagelocations, String[] commands,int maxpages){
		pid=PID;
		turns=0;
		subqueue=1;
		abnormalTermination=false;
		elapsetime=0;
		loadedPages=0;
		memory=new Page[maxpages];
		allotedPageLocations=pagelocations;
		Commands=commands;
		for(int i=0;i<maxpages;i++){
			Page page=new Page();
			memory[i]= page;
		}
		Maxmemory=memory.length;
	}
	public int leastUsed(){
			int temp=10000000;
			int k=-1;
			for(int i=0;i<memory.length;i++){
				if(memory[i].res){
					if(memory[i].access<temp){
						k=i;
						temp=memory[i].access;
					}
				}
			}
			if (k<1)
			{
				k=1;
			}
			return k;
	}
	public boolean pageIsResident(int i){
	for(int j=0;j<memory.length-1;j++){
		if(memory[j].res){
			return true;
		}
	}
	return false;
	}
	public boolean validAccess(int i){
	if(i>memory.length){
		return false;
	}
	return true;
	}
	public boolean Loadpage(int i){
		if(loadedPages<(memory.length/4)){
			memory[i].res=true;
			memory[i].location=allotedPageLocations[loadedPages];
			memory[i].access=0;
			return true;
		}
		else
		{	
			int temp= memory[leastUsed()].location;
			memory[leastUsed()].res=false;
			memory[leastUsed()].access=0;
			memory[i].res=true;
			memory[i].location=temp;
			return true;
		}
	}
	public void Run(){
		char command=Commands[instructionPointer].charAt(0);
		int address=Integer.parseInt(Commands[instructionPointer].substring(1));
		if(!validAccess(address)){
			abnormalTermination=true;
		}
		else if(pageIsResident(address)){
			if(command=='p'){
				memory[address].access++;
				elapsetime+=COM_RUN_TIME;
			}
			else if(command=='w'){
				polling=true;
				memory[address].access++;
				elapsetime+=COM_RUN_TIME;
			}
			else if(command=='r'){
				polling=true;
				memory[address].access++;
				elapsetime+=COM_RUN_TIME;
			}
			else{
				abnormalTermination=true;
			}
		}
		else{
			Loadpage(address);
			elapsetime+=PAGE_GET_TIME;
		}
		
	}
}
