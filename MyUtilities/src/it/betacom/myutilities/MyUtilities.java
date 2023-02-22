package it.betacom.myutilities;



import java.util.Comparator;
import java.util.List;
import java.time.LocalDate;
import java.util.Random;



public class MyUtilities {


	
	public static String getExtension( String file ) {
		int i = file.lastIndexOf(".");
		return i == -1 ? "" : file.substring(i+1);
	}



    public static <T> void swap ( List<T> list, int i, int j ) {
		T temp = list.get(i);
		list.set( i, list.get(j) );
		list.set( j, temp );
	}



	public static <T> T medianOfThree( List<T> list, Comparator<? super T> comp, int i, int j, int l ) {
		if( comp.compare( list.get(i), list.get(j) ) > 0 )
			swap(list, i, j);
		if( comp.compare( list.get(i), list.get(l) ) > 0 )
			swap(list, i, l);
		if( comp.compare( list.get(j), list.get(l) ) > 0 )
            swap(list, j, l);
		return list.get(j);
	}



// RANDOM VALUES
	public static float nextFloat(float low, float high) {
		Random rand = new Random();
		return low + (high - low) * rand.nextFloat();
	}


	public static int nextInt(int low, int high) {
		Random rand = new Random();
		return rand.nextInt(high - low + 1) + low;
	}


	public static LocalDate nextDate() {
		return LocalDate.of( nextInt(1970, 2000), nextInt(1, 12), nextInt(1, 28) );
	}



// SORTING
	public static <T> void insertionSort( List<T> list, Comparator<? super T> comp, int b, int e ) {
		int i, j;
		T val;
		for( i = b+1; i <= e; ++i ) {
			val = list.get(i);
            for(j = i; j > b && comp.compare( list.get(j-1), val) > 0; --j )
                list.set( j, list.get(j-1) );
            list.set(j, val);
		}
	}



	public static <T> int hoarePartition( List<T> list, Comparator<? super T> comp, int s, int f ) {
		int i = s - 1,
            j = f + 1,
			d = (f - s) / 3;
		T p = medianOfThree( list, comp,
			nextInt( s, s+d-1 ),
			nextInt( s+d, s+(2*d) ),
			nextInt( s+(2*d)+1, f )
		);
		while(true) {
			do { ++i; } while( comp.compare( list.get(i), p ) < 0 );
			do { --j; } while( comp.compare( list.get(j), p ) > 0 );
			if( i >= j )
				return j;
			swap(list, i, j);
		}
	}


	public static <T> void quicksort( List<T> list, Comparator<? super T> comp, int b, int e ) {
		while(b < e) {
            if( e - b < 9 ) {
                insertionSort(list, comp, b, e);
                break;
            }
            else {
                int p = hoarePartition(list, comp, b, e);
                if( p - b < e - p ) {
                    quicksort(list, comp, b, p);
                    b = p + 1;
                }
                else {
                    quicksort(list, comp, p+1, e);
                    e = p;
                }
            }
        }
	}

}