
/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 *
 * 구조: 2단계 연결 리스트
 * - 1단계: Genre들의 정렬된 연결 리스트 (genreList)
 * - 2단계: 각 Genre 내부에 Movie들의 정렬된 연결 리스트 (movieList)
 *
 * 모든 데이터는 삽입 시점에 정렬된 위치에 삽입되어 항상 정렬 상태 유지
 */
public class MovieDB {
	// 장르들을 알파벳 순으로 정렬하여 저장하는 연결 리스트
	private MyLinkedList<Genre> genreList;

    public MovieDB() {
        this.genreList = new MyLinkedList();
    }

    public void insert(MovieDBItem item) {
		String genre = item.getGenre();
		String title = item.getTitle();

		// 삽입할 장르의 정렬된 위치를 찾음
		Node<Genre> currGenre = genreList.head;
		while (currGenre.getNext() != null) {
        	String nextGenre = (String) currGenre.getNext().getItem().getItem();
        	if (genre.compareTo(nextGenre) <= 0) {
            	break; // 삽입 위치 찾음
        	}
        	currGenre = currGenre.getNext();
		}

		// 해당 장르가 이미 존재하는지 확인
		String nextGenre = null;
		if (currGenre.getNext() != null && currGenre.getNext() != null) {
    		nextGenre = (String) currGenre.getNext().getItem().getItem();
		}

		// 장르가 없으면 새로 생성, 있으면 기존 장르 사용
		if ((nextGenre != null && genre.compareTo(nextGenre) < 0) || nextGenre == null) {
			// 새 장르 생성 및 삽입
			Genre newGenre = new Genre(genre);
			currGenre.insertNext(newGenre);
			currGenre = currGenre.getNext();
		} else {
			// 기존 장르 사용
			currGenre = currGenre.getNext();
		}

		// 해당 장르의 영화 리스트에 제목 추가 (정렬된 위치에 삽입)
		currGenre.getItem().getMovieList().add(title);
    }

    public void delete(MovieDBItem item) {
		String genre = item.getGenre();
		String title = item.getTitle();

		// 해당 장르를 찾음
		int flag = 0;
		Node<Genre> prevNode = genreList.head;
		Node<Genre> currNode = null;

		while (prevNode.getNext() != null) {
			currNode = prevNode.getNext();
			if (currNode.getItem().getItem().compareTo(genre) == 0) {
				flag = 1; // 장르 찾음
				// 만약 장르가 없다면 무시됨
				break;
			}
			prevNode = currNode;
		}

		// 장르가 존재하면, 해당 장르 내에서 영화 제목을 찾아 삭제
		if (flag == 1 && currNode != null) {
			Node<String> prevMovie = currNode.getItem().getMovieList().head;

			while (prevMovie.getNext() != null) {
				Node<String> currMovie = prevMovie.getNext();
				if (currMovie.getItem().compareTo(title) == 0) {
					// 영화 삭제
					// 없다면 무시됨
					prevMovie.setNext(currMovie.getNext());
					break;
				}
				prevMovie = currMovie;
			}
		}

		// 장르 내 영화가 모두 삭제되었으면 장르도 삭제
		if (currNode != null && currNode.getItem().getMovieList().isEmpty()) {
			prevNode.setNext(currNode.getNext());
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
		MyLinkedList<MovieDBItem> results = new MyLinkedList();

		// 모든 장르를 순회
		Node<Genre> currGenre = genreList.head;
		while (currGenre.getNext() != null) {
			currGenre = currGenre.getNext();

			// 해당 장르 내 모든 영화를 순회
			Node<String> currMovie = currGenre.getItem().getMovieList().head;
			while (currMovie.getNext()  != null) {
				currMovie = currMovie.getNext();

				// 영화 제목에 검색어가 포함되어 있으면 결과에 추가
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

		// 모든 장르를 순회
		Node<Genre> currGenre = genreList.head;
		while (currGenre.getNext() != null) {
			currGenre = currGenre.getNext();

			// 해당 장르 내 모든 영화를 순회하여 결과에 추가
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

/**
 * Genre 클래스
 * - Node<String>을 상속하여 장르 이름을 저장
 * - 각 장르는 내부에 해당 장르의 영화들을 저장하는 MovieList를 가짐
 */
class Genre extends Node<String> implements Comparable<Genre> {
	private final MovieList movieList;

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

/**
 * MovieList 클래스
 * - MyLinkedList<String>을 상속하여 영화 제목들을 저장
 * - add() 메소드를 오버라이드하여 알파벳 순으로 정렬된 위치에 삽입
 * - 중복된 제목은 예외 발생
 */
class MovieList extends MyLinkedList<String> {
	@Override
	public void add(String title) {
		int flag = 0;
		// 정렬된 위치 찾기
		Node currNode = head;
		while (currNode.getNext() != null) {
        	String nextTitle = (String) currNode.getNext().getItem();
        	if (title.compareTo(nextTitle) < 0) {
            	break; // 삽입 위치 찾음
        	} else if (title.compareTo(nextTitle) == 0) {
				flag = 1; // 중복 제목일 때는 무시
			}
        	currNode = currNode.getNext();
		}
		// 정렬된 위치에 삽입
		if (flag == 0) {
			currNode.insertNext(title);
			numItems++;
		}
	}
}