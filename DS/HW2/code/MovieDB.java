
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
    }

    public void delete(MovieDBItem item) {

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

		if (currNode != null && currNode.getItem().getMovieList().isEmpty()) {
			prevNode.setNext(currNode.getNext());
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
		MyLinkedList<MovieDBItem> results = new MyLinkedList();

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