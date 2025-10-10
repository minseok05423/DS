import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
	private MyLinkedList<Genre> genreList;

    public MovieDB() {
        this.genreList = new MyLinkedList();
		// FIXME implement this
    	
    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한 
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
    }

    public void insert(MovieDBItem item) {
		String genre = item.getGenre();
		String title = item.getTitle();

		Node<Genre> currGenre = genreList.head;
		while (currGenre.getNext() != null) {
        	String nextGenre = (String) currGenre.getNext().getItem().getItem();
        	if (genre.compareTo(nextGenre) <= 0) {
            	break; 
        	}
        	currGenre = currGenre.getNext();
		}

		String nextGenre = null;
		if (currGenre.getNext() != null && currGenre.getNext() != null) {
    		nextGenre = (String) currGenre.getNext().getItem().getItem();
		}		
		
		if ((nextGenre != null && genre.compareTo(nextGenre) < 0) || nextGenre == null) {
			Genre newGenre = new Genre(genre);
			currGenre.insertNext(newGenre);
			currGenre = currGenre.getNext();
		} else {
			currGenre = currGenre.getNext();			
		}
		
		currGenre.getItem().getMovieList().add(title);

        // FIXME implement this
        // Insert the given item to the MovieDB.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
    }

    public void delete(MovieDBItem item) {
		Iterator<Genre> Genreiter = genreList.iterator();

		String genre = item.getGenre();
		String title = item.getTitle();

		int flag = 0;

		Node<Genre> prevNode = genreList.head;
		Node<Genre> currNode = null;

		while (prevNode.getNext() != null) {
			currNode = prevNode.getNext();
			if (currNode.getItem().getItem().compareTo(genre) == 0) {
				flag = 1;
				break;
			}
			prevNode = currNode;
		}

		if (flag == 1 && currNode != null) {
			Node<String> prevMovie = currNode.getItem().getMovieList().head;

			while (prevMovie.getNext() != null) {
				Node<String> currMovie = prevMovie.getNext();
				if (currMovie.getItem().compareTo(title) == 0) {
					prevMovie.setNext(currMovie.getNext());
					break;
				}
				prevMovie = currMovie;
			}
		}

		if (currNode.getItem().getMovieList().isEmpty()) {
			prevNode.setNext(currNode.getNext());
		}

        // FIXME implement this
        // Remove the given item from the MovieDB.
    	
    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
    }

    public MyLinkedList<MovieDBItem> search(String term) {
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

		Node<Genre> currGenre = genreList.head;
		while (currGenre.getNext() != null) {
			currGenre = currGenre.getNext();
			Node<String> currMovie = currGenre.getItem().getMovieList().head;
			while (currMovie.getNext()  != null) {
				currMovie = currMovie.getNext();
				if (currMovie.getItem().contains(term)) {
					MovieDBItem newItem = new MovieDBItem(currGenre.getItem().getItem(), currMovie.getItem());
					results.add(newItem);
				}
			}
		}

        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
    	
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.
    	
        // This tracing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
    	
    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        

        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
		MyLinkedList<MovieDBItem> results = new MyLinkedList();

		Node<Genre> currGenre = genreList.head;
		while (currGenre.getNext() != null) {
			currGenre = currGenre.getNext();
			Node<String> currMovie = currGenre.getItem().getMovieList().head;
			while (currMovie.getNext()  != null) {
				currMovie = currMovie.getNext();
				MovieDBItem newItem = new MovieDBItem(currGenre.getItem().getItem(), currMovie.getItem());
				results.add(newItem);
			}
		}
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.

    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   
        
    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	private MovieList movieList;

	public Genre(String name) {
		super(name);
		this.movieList = new MovieList();
	}

	public MovieList getMovieList() {
		return movieList;
	}
	
	@Override
	public int compareTo(Genre o) {
		return this.getItem().compareTo(o.getItem());
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

class MovieList extends MyLinkedList<String> {
	@Override
	public void add(String title) {
		Node currNode = head;
		while (currNode.getNext() != null) {
        	String nextTitle = (String) currNode.getNext().getItem();
        	if (title.compareTo(nextTitle) < 0) {
            	break; 
        	} else if (title.compareTo(nextTitle) == 0) {
				throw new IllegalArgumentException("Movie already exists in this title");
			}
        	currNode = currNode.getNext();
		}
		currNode.insertNext(title);
		numItems++;
	}
}