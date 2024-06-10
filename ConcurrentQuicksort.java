package datos;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ConcurrentQuicksort {
	//ALGORITMO DE ORDENAMIENTO QUICKSORT CONCURRENTE
		public static class QuicksortTask extends RecursiveTask<int[]> {
	        private final int[] array;

	        public QuicksortTask(int[] array) {
	            this.array = array;
	        }

	        //Si el array tiene un sólo elemento o está vacío, se devuelve tal cual
	        //De lo contrario, se elije un pivote y se particiona el array en 3 
	        //según los elementos sean menores, mayores o iguales al pivote
	        @Override
	        protected int[] compute() {
	            if (array.length <= 1) {
	                return array;
	            }
	            
	            //Se elige el pivote, en este caso, el elemento del medio
	            int pivot = array[array.length / 2];
	            
	            //Se particiona el array en 3 subarrays
	            //El subarray left, filtra los elementos menores al pivote
	            int[] left = Arrays.stream(array).filter(x -> x < pivot).toArray();
	            
	            //El subarray middle filtra los elementos iguales al pivote
	            int[] middle = Arrays.stream(array).filter(x -> x == pivot).toArray();
	            
	            //El subarray right filtra los elementos mayores al pivote
	            int[] right = Arrays.stream(array).filter(x -> x > pivot).toArray();

	            //Se crean tareas de tipo QuickSortTask, para las partes "left" y
	            //"right" y se ejecutan en paralelo con la función invokeAll()
	            QuicksortTask leftTask = new QuicksortTask(left);
	            QuicksortTask rightTask = new QuicksortTask(right);

	            invokeAll(leftTask, rightTask);

	            //Esperamos a que las tareas concurrentes se completen
	            int[] leftSorted = leftTask.join();
	            int[] rightSorted = rightTask.join();

	            //una vez que están completadas las tareas, se usa
	            //la función concatenate() para combinar los 3 arrays ordenados
	            return concatenate(leftSorted, middle, rightSorted);
	        }

	        //Esta funcion se utiliza para concatenar 3 arrays que se pasen como parámetro
	        private int[] concatenate(int[] left, int[] middle, int[] right) {
	            int[] result = new int[left.length + middle.length + right.length];
	            System.arraycopy(left, 0, result, 0, left.length);
	            System.arraycopy(middle, 0, result, left.length, middle.length);
	            System.arraycopy(right, 0, result, left.length + middle.length, right.length);
	            return result;
	        }
	    }

		//Este método crea un ForkJoinPool (pileta de hilos) y lo usa para
		//invocar la tarea de ordenamiento
	    public static int[] concurrentQuicksort(int[] array) {
	        ForkJoinPool pool = new ForkJoinPool();
	        return pool.invoke(new QuicksortTask(array));
	    }
		
}
