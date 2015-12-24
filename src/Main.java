import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	public static Directory mainDir ;
	public static boolean[] blocks;
	public static String allocationMethod ;
	
	public static BufferedWriter bw ;
	public static FileWriter fw ;
	
	public static BufferedReader br ;
	public static FileReader fr ;
	
	public static File file ;
	
	public static Filee[] addFile(Filee[] org, Filee added) {
		
	    Filee[] result = Arrays.copyOf(org, org.length +1);
	    result[org.length] = added;
	    
	    return result;
	}
	
	public static Directory[] addDirectory(Directory[] org, Directory added) {
		
		Directory[] result = Arrays.copyOf ( org , org.length + 1 ) ;
	    result[org.length] = added ;
	    
	    return result ;
	}
	
	public static int getWorstFit ( int size ){
		
		int max = 0 , idx = -1 ;
		int cnt = 0 ;

		for ( int i = 0 ; i < blocks.length ; i++) {
			
			if ( blocks[i] && cnt > max ) {
				
				max = cnt ;
				idx = i - cnt ;
				cnt = 0 ;
			} 
			
			else {
				
				cnt ++ ;
			}
		}
		
		if ( !blocks[blocks.length-1] && cnt > max ){
			
			max = cnt ;
			idx = blocks.length - cnt ;
			cnt = 0 ;
		}
		if ( max < size ){
			
			return -1 ;
		}
		return idx;
	}

	public static int[] getIndexedBlocks ( int size ){
		
		int[] allocated = new int[size+1] ;
		int k = 0 , i=0 ;
		
		for ( ; k<( size + 1) ; i++ ){
			
			if ( !blocks[i] ){
				
				allocated[k] = i ;
				k ++ ;
			}
		}
		
		if ( k - 1 == size )
			return allocated ;
		
		allocated = new int[1] ;
		
		return allocated ;
	}
	
	public static boolean createFileByCont ( String name , int size ){
	
		int idx = getWorstFit ( size ) ;
		
		String[] path = name.split("/") ;
		
		if ( idx == -1 ){
			//thrw error
			//System.out.println(15);
			return false;
		}
		
		if ( !path[0].equals("root") ){
			//System.out.println(65);
			return false;
		}
		
		// erase file name from all path
		path = Arrays.copyOf ( path , path.length - 1 ) ;
		
		// reJoin path array to get directory path 
		String tempName = String.join("/", path);
		
		Directory tempDir = new Directory() ;
		tempDir = findDir ( mainDir , tempName ) ;
		
		if ( tempDir.directoryPath.equals("Not Exist") ){
			//System.out.println(50);
			return false ;
		}
		
		Filee temp = new Filee() ;
		
		temp.filePath = name ;
		temp.allocatedBlocks = new int[size] ;
		
		for ( int j=idx , k=0 ; j< (idx + size) ; j++ , k++ ){
			
			temp.allocatedBlocks[k] = j ;
			blocks[j] = true ;
		}
		
		tempDir.files = addFile ( tempDir.files , temp ) ;
		
		return true ;
	}

	public static boolean createFileByIndexed ( String name , int size ){
		
		int[] temp = getIndexedBlocks ( size ) ;
		
		String[] path = name.split("/") ;
		
		if ( temp.length != size + 1 ){
			//thrw error
			//System.out.println(15);
			return false;
		}
		
		if ( !path[0].equals("root") ){
			//System.out.println(65);
			return false;
		}
		
		// erase file name from all path
		path = Arrays.copyOf ( path , path.length - 1 ) ;
		
		// reJoin path array to get directory path 
		String tempName = String.join("/", path);
		
		Directory tempDir = new Directory() ;
		tempDir = findDir ( mainDir , tempName ) ;
		
		if ( tempDir.directoryPath.equals("Not Exist") ){
			//System.out.println(50);
			return false ;
		}
		
		Filee tempFile = new Filee() ;
		
		tempFile.filePath = name ;
		tempFile.allocatedBlocks = new int[temp.length] ;
		tempFile.allocatedBlocks = temp ;
		
		tempDir.files = addFile ( tempDir.files , tempFile ) ;
		
		for ( int i=0 ; i<temp.length ; i++ ){
			
			blocks[ temp[i] ] = true ;
		}
		
		return true ;
	}
	
	public static boolean createFolder ( String name ) {
		
		String[] path = name.split("/") ;
		
		if ( !path[0].equals("root") ){

			return false ;
		}
		
		// erase directory name from all path
		path = Arrays.copyOf ( path , path.length - 1 ) ;
		
		// reJoin path array to get directory path 
		String tempName = String.join("/", path);
		
		Directory tempDir = new Directory() ;
		tempDir = findDir ( mainDir , tempName ) ;
		
		if ( tempDir.directoryPath.equals("Not Exist") )
			return false ;
		
		Directory temp = new Directory() ;
		
		temp.directoryPath = name ;
		temp.files = new Filee[0] ;
		temp.subDirectories = new Directory[0] ;
		
		tempDir.subDirectories = addDirectory ( tempDir.subDirectories , temp ) ;
		
		return true ;
	}
	
	public static boolean deleteFile ( String name ){
		
		String[] path = name.split("/");
		
		Filee tempFile = new Filee() ;
		tempFile = findFile ( mainDir , name ) ;
		
		if ( tempFile.filePath.equals("Not Exist" ) ){
			
			return false ;
		}
		
		for ( int i=0 ; i<(tempFile.allocatedBlocks).length ; i++ ){
			
			blocks[ tempFile.allocatedBlocks[i] ] = false ;
		}
		
		tempFile.deleted = true ;
		
		return true ;
	}
	
	public static boolean deleteFolder ( String name ){
		
		String[] path = name.split("/");
		
		Directory tempDir = new Directory() ;
		tempDir = findDir ( mainDir , name ) ;
		
		if ( tempDir.directoryPath.equals("Not Exist" ) ){
			
			return false ;
		}
		
		tempDir.deleted = true ;
		
		return true ;
	}
	
	public static Directory findDir ( Directory d , String path ){
		
		Directory res = new Directory() ;
		res.directoryPath = "Not Exist" ;
		
		if ( d.directoryPath.equals ( path ) )
			return d ;
		
		for ( Directory dir : d.subDirectories ) {
			
			if ( dir.directoryPath.equals ( path ) ){
				
				return dir ;
			}
			
			findDir ( dir , path ) ;
		}
		
		return res ;
	}
	
	public static Filee findFile ( Directory d , String path ){
		
		Filee res = new Filee() ;
		res.filePath = "Not Exist" ;
		
		for (Filee file : d.files ) {
			
			//System.out.println( file.filePath );
			
			if ( file.filePath.contains ( path ) ){
				
				return file ;
			}
		}
		
		for ( Directory dir : d.subDirectories ) {
			
			//System.out.println( dir.directoryPath );
			//System.out.println( d.directoryPath.contains ( path ) );
			
			for (Filee file : dir.files ) {
				
				//System.out.println( file.filePath );
				
				if ( file.filePath.contains ( path ) ){
					
					return file ;
				}
			}
			
			findFile ( dir , path ) ;
		}
		
		return res ;
	}
	
	public static void writeFileSystemStructure ( Directory dir , int level ){
		
		try {
			
			// if file doesnt exists, then create it
			if (!file.exists())
				file.createNewFile();
		
			bw.write ( "*" + dir.directoryPath ) ;
			bw.newLine();
			
			for ( Filee file1 : dir.files ){
				
				if ( !file1.deleted ){
					
					bw.write ( "-" + file1.filePath ) ;
					bw.newLine();
				}
			}
			
			if ( dir.subDirectories.length == 0 )
				return ;
			
			for ( Directory d : dir.subDirectories ) {
				
				if ( !d.deleted )
					writeFileSystemStructure( d , level + 4 ) ;
			}
		}
		catch (IOException e1) {
			
			e1.printStackTrace();
		}
	}

	
	public static void readFileSystem ( Directory dir ){
		
		Directory tempDir = mainDir ;
		
		tempDir.files = new Filee[0] ;
		tempDir.subDirectories = new Directory[0] ;
		
		try {
			
			String line = "" ;

			while ( (line = br.readLine()) != null ){
				
				if ( line.charAt(0) == '-' ){
					
					Filee temp = new Filee() ;
					temp.filePath = line.substring(1, line.length()) ;
					
					tempDir.files = addFile( tempDir.files , temp ) ;
					System.out.println ( line ) ;
				}
				else {
					
					tempDir = mainDir ;
					
					tempDir.files = new Filee[0] ;
					tempDir.subDirectories = new Directory[0] ;
				}
			}
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		int N = in.nextInt();
		in.nextLine();
		allocationMethod = in.nextLine() ;
		
		try {
			
			if ( allocationMethod.equals ( "contiguos" ) )
				file = new File("Contiguos File System.txt");
			
			else 
				file = new File("Indexed File System.txt");
			
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			fr = new FileReader(file.getAbsoluteFile()) ;
			br = new BufferedReader(new FileReader("C:\\testing.txt")) ;
		} 
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		blocks = new boolean[N];
		
		Arrays.fill ( blocks , false ) ;
		
		mainDir = new Directory() ;
		
		mainDir.directoryPath = "root" ;
		mainDir.subDirectories = new Directory[0] ;
		mainDir.files = new Filee[0] ;
		
		createFileByCont ( "root/7amada.txt" , 5 ) ;
		
		createFolder ( "root/secondRoot" ) ;
		
		createFileByCont ( "root/secondRoot/tita.txt", 5 ) ;
		createFileByCont ( "root/secondRoot/kita.txt", 5 ) ;
		createFileByCont ( "root/secondRoot/sita.txt", 5 ) ;
		
		writeFileSystemStructure ( mainDir , 0 ) ;
		
		try {
			
			bw.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mainDir.printDirectoryStructure ( mainDir , 0 ) ;
	}
}
