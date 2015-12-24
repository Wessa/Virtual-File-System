
public class Directory {

	public String directoryPath ;
	public Filee[] files ;
	public Directory[] subDirectories ;
	public boolean deleted = false ;
	
	public Directory(){

	}
	
	public void printDirectoryStructure ( Directory d , int level ){
		
		for ( int i=0 ; i<level; i++ )
			System.out.print(" ");
		
		String[] tempDirs = d.directoryPath.split("/") ;
		
		System.out.println( tempDirs[tempDirs.length-1] );
		
		for ( Filee file : d.files ) {
			
			if ( !file.deleted ){
				
				for ( int i=0 ; i< ( level + 4 ); i++ )
					System.out.print(" ");
			
				String[] tempFiles = file.filePath.split("/") ;
			
				System.out.println( tempFiles[tempFiles.length-1] );
			}
		}
		
		if ( d.subDirectories.length == 0 )
			return ;
		
		for ( Directory dir : d.subDirectories ) {
			
			if ( !dir.deleted )
				printDirectoryStructure ( dir , level + 4 ) ;
		}
		
	}
	
	public String toString(){
		
		return directoryPath ;
	}

}
