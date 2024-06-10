package datos;

public class SequentialQuicksort {
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
		//su izquierda, y los mayores se coloquen a su derecha.
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
}
