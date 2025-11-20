import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
Pattern Search (length > 6):
 - The pattern is split into overlapping 6-char chunks.
 - For each chunk we locate its node via hash+search; if any chunk is absent, no match.
 - Candidate positions are taken from the first chunk's occurrence list.
 - For each candidate position we re-check the full pattern against the stored line text.
   (This verification step ensures alignment of all chunks without storing longer substrings.)
 */

public class Matching
{
	private static AVLTree<String>[] hashMap; // 100 hash buckets, each an AVLTree of 6-char keys
	private static final int patternLength = 6; // fixed window size for indexing
	private static List<String> fileLines; // full lines retained for pattern verification (>6)
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{	
		if (input.equals("QUIT")) {
			System.exit(0);
		}

		String[] parts = input.split(" ", 2);
		String operation = parts[0];
		String argument = parts.length > 1 ? parts[1] : null;

		operation(operation, argument);
	}

	private static void operation(String operation, String argument) {
		switch (operation) {
			case "<":
				readFile(argument);
				break;
			case "@":
				readHashIndex(argument);
				break;
			case "?":
				patterSearch(argument);
				break;
			default:
				System.out.println("Unknown operation: " + operation);
		}
	}

	/*
	Reads a text file and indexes all 6-character substrings
	Each substring position stored as "(line, column)" (1-based indices)
	 */
	private static void readFile(String filename) {
		try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
			hashMap = new AVLTree[100];
			fileLines = new ArrayList<>();
			String line;
			int lineNumber = 1;
			while ((line = fileReader.readLine()) != null) {
				fileLines.add(line);
				for (int j = 0; j <= line.length() - patternLength; j++) {
					String sub = line.substring(j, j + patternLength);
					int hashIndex = getHashIndex(sub);
					if (hashMap[hashIndex] == null) {
						hashMap[hashIndex] = new AVLTree<String>();
					}
					AVLTree<String> tree = hashMap[hashIndex];
					String pos = "(" + lineNumber + ", " + (j + 1) + ")";
					tree.insert(sub, pos);
				}
				lineNumber++;
			}
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
		}
	}

	private static void readHashIndex(String index) {
		int idx = Integer.parseInt(index);
		LinkedList<String> result = new LinkedList<>();

		if (idx < 0 || idx >= 100) {
			System.out.println("Invalid index. Please provide an index between 0 and 99.");
			return;
		}

		AVLTree<String> tree = hashMap[idx];
		if (tree == null) {
			System.out.println("EMPTY");
			return;
		}
		tree.rootTraverse(result);

		String output = String.join(" ", result);
		System.out.println(output);
	}

	/*
	Pattern search entry. Handles two cases:
	- length == 6: direct key lookup
	- length > 6 : chunk-based lookup + full verification
	*/
	private static void patterSearch(String pattern) {
		if (pattern.length() < 6) {
			System.out.println("ERROR");
			return;
		}

		// For patterns of exactly 6 characters, search directly
		if (pattern.length() == 6) {
			int idx = getHashIndex(pattern);
			AVLTree<String> tree = hashMap[idx];

			if (tree == null) {
				System.out.println("(0, 0)");
				return;
			}

			AVLNode<String> targetNode = tree.search(pattern);
			if (targetNode == null || targetNode.list.isEmpty()) {
				System.out.println("(0, 0)");
				return;
			}

			String output = String.join(" ", targetNode.list);
			System.out.println(output);
			return;
		}

		// For patterns longer than 6, split into 6-char chunks and intersect positions
		int numChunks = pattern.length() - 6 + 1;
		List<List<String>> chunkPositions = new ArrayList<>();
		for (int i = 0; i < numChunks; i++) {
			String chunk = pattern.substring(i, i + 6);
			int idx = getHashIndex(chunk);
			AVLTree<String> tree = hashMap[idx];
			if (tree == null) {
				System.out.println("(0, 0)");
				return;
			}
			AVLNode<String> node = tree.search(chunk);
			if (node == null || node.list.isEmpty()) {
				System.out.println("(0, 0)");
				return;
			}
			chunkPositions.add(node.list);
		}

		// Intersect positions: only keep those where all chunks align
		LinkedList<String> validPositions = new LinkedList<>();
		if (!chunkPositions.isEmpty()) {
			// Use first chunk's positions as base
			for (String posStr : chunkPositions.get(0)) {
				if (isFullPatternAtPosition(pattern, posStr)) {
					validPositions.add(posStr);
				}
			}
		}

		if (validPositions.isEmpty()) {
			System.out.println("(0, 0)");
		} else {
			String output = String.join(" ", validPositions);
			System.out.println(output);
		}
	}

	// Helper: check if full pattern matches at given position
	private static boolean isFullPatternAtPosition(String pattern, String posStr) {
		if (fileLines == null) return false;
		try {
			String inner = posStr.substring(1, posStr.length() - 1);
			String[] parts = inner.split(",");
			if (parts.length != 2) return false;
			int line = Integer.parseInt(parts[0].trim());
			int col = Integer.parseInt(parts[1].trim()); // 1-based
			if (line < 1 || line > fileLines.size()) return false;
			String lineText = fileLines.get(line - 1);
			int startIdx = col - 1;
			int endIdx = startIdx + pattern.length();
			if (startIdx < 0 || endIdx > lineText.length()) return false;
			return lineText.substring(startIdx, endIdx).equals(pattern);
		} catch (Exception e) {
			return false;
		}
	}

	// Legacy helper kept for reference, not used by current logic.
	private static boolean verifyPatternAtPosition(String pattern, String posStr) {
		return isFullPatternAtPosition(pattern, posStr);
	}

	private static int getHashIndex(String input) {
		int result = 0;
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			result += (int) ch;
		}

		return result % 100;
	}
}
