
public class UsersArray<T> {
	int sp; // empty stack
	protected T[] head; // array
	private int size;

	 @SuppressWarnings("unchecked")
	public UsersArray() {
	    size = 24; // sets the default size of the stack
	    head = (T[]) new String[size];
	    sp = -1;
	 }

	 public boolean isFull() {
		 return sp == this.size -1;
	 }

	 public boolean isEmpty() {
	    return sp == -1;
	 }

	 public void push(T t) {
		 if(!isFull()) {
	       	 head[++sp] = t;
	       	 System.out.println(t + " Created");
	       	 System.out.println(sp);
		 } else {
			 System.out.println("Array full...");
		 }
	 }

	 public T pop() {
		if (isEmpty()) {
			return null;
	     } else
	       return head[sp--];
	 }
	 public int size() {
		 return head.length;
	 }
	 public T getHead(int i) {
		 return head[i];
	 }
	 public T[] returnUsers() {
		 return head;
	 }
}
