import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ChainMenu {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty = 5;
	private static String ip = null;

	public static void main(String[] args) {	
		//add our blocks to the blockchain ArrayList:
		DataInputStream input = new DataInputStream(System.in);
		boolean continueLoop = true;
		
		try {
		System.out.println("Enter IP of distributed node");
		ip = input.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//Genesis block
		addBlock(new Block("Genesis"));
		
		while(continueLoop) {
			
			System.out.println("1. Create and Mine Block");
			System.out.println("2. Check Chain validity");
			System.out.println("3. Print Chain");
			System.out.println("4. Fetch Updates");
			System.out.println("5. Broadcast updates");
			System.out.println("6. Exit");
			try {
				Integer in = Integer.parseInt(input.readLine());
				switch(in) {
				case 1:
					System.out.println("Enter block data");
					String data = input.readLine();
					System.out.println("Trying to Mine block " + (blockchain.size()+1) + "...");
					addBlock(new Block(data, blockchain.get(blockchain.size()-1).hash));
					break;
				case 2:
					if(isChainValid())
						System.out.println("The chain is valid!");
					else
						System.out.println("The chain is NOT valid!");
					break;
				case 3:
					String blockchainJson = StringUtil.getJson(blockchain);
					System.out.println("\nThe block chain: ");
					System.out.println(blockchainJson);
					break;
				case 4:
					Client client = new Client(ip, 5000);
					String update = client.fetchData();
					ArrayList<Block> newchain = StringUtil.decodeJson(update);
					blockchain = (newchain.size() > blockchain.size())? newchain: blockchain;
					break;
				case 5:
					new Server(5000, StringUtil.getJson(blockchain));
					break;
				case 6:
					continueLoop = false;
					break;
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
//		System.out.println("Trying to Mine block 1... ");
//		addBlock(new Block("Hi im the first block", "0"));
//		
//		System.out.println("Trying to Mine block 2... ");
//		addBlock(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
//		
//		System.out.println("Trying to Mine block 3... ");
//		addBlock(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));	
//		
//		System.out.println("\nBlockchain is Valid: " + isChainValid());
//		
//		String blockchainJson = StringUtil.getJson(blockchain);
//		System.out.println("\nThe block chain: ");
//		System.out.println(blockchainJson);
	}
	
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		//loop through blockchain to check hashes:
		
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
			
		}
		return true;
	}
	
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}
