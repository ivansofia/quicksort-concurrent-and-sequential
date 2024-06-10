package test;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Test {
	
	//ALGORITMO DE ORDENAMIENTO QUICKSORT SECUENCIAL
	//El proceso básico de Quicksort es seleccionar un elemento como pivote y
	//particionar el array en dos subarrays, uno con elementos menores que el
	//pivote y otro con elementos mayores.
	//Luego, se aplica recursivamente el mismo proceso a los subarrays.
	public static void sequentialQuicksort(int[] arr, int low, int high) {
		if (low < high) {
			int pi = partition(arr, low, high);
			sequentialQuicksort(arr, low, pi - 1);
			sequentialQuicksort(arr, pi + 1, high);
		}
	}

	
	//La función principal de partition es reorganizar los elementos de tal
	//manera de que todos los elementos menores que el pivote se coloquen a 
	//su izquierda, y los mayores se colocen a su derecha.
	//Además, coloca el pivote en la posición correcta en el array ordenado.
	public static int partition(int[] arr, int low, int high) {
		//seleccionar el pivote
		int pivot = arr[high];
		
		//inicialización del índice
		int i = low - 1;
		
		//Se recorren todos los elementos del subarray desde low hasta high-1
		for (int j = low; j < high; j++) {
			//Este if() compara los elementos del array con el pivote
			if (arr[j] < pivot) {
				i++;
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
		
		//coloca el pivote en su posicion correcta
		int temp = arr[i + 1];
		arr[i + 1] = arr[high];
		arr[high] = temp;
		
		return i + 1;
	}
	
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
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Variables para medir tiempo
		double initialTimeSec;
        double finalTimeSec;
        double initialTimeConc;
        double finalTimeConc;
        
		//ARRAY CON NUMEROS RANDOM
        int tam = 10;
		int[] randomArray = new int[tam];
        for (int i = 0; i < randomArray.length; i++) {
            randomArray[i] = (int) (Math.random() * 100);
        }
       
        System.out.println("Cantidad de elementos del array: " + tam);
		//QUICKSORT SECUENCIAL con medidas de tiempo
		initialTimeSec = System.nanoTime() / 1000;
		sequentialQuicksort(randomArray, 0, randomArray.length-1);
		finalTimeSec = (System.nanoTime() / 1000) - initialTimeSec;
		System.out.println("Algoritmo secuencial. Tiempo: " + finalTimeSec);
		
		//QUICKSORT CONCURRENTE con medidas de tiempo
		initialTimeConc = System.nanoTime() / 1000;
		concurrentQuicksort(randomArray);
		finalTimeConc = (System.nanoTime() / 1000) - initialTimeConc;
		System.out.println("Algoritmo concurrente. Tiempo: " + finalTimeConc);
	}
}
