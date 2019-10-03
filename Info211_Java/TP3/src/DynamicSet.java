import java.util.HashSet;

public class DynamicSet {
    private HashSet<Integer> set;
    private int size;

    public DynamicSet(int size){
        this.size = size;
        set = new HashSet<>();
    }

    public boolean add(int el){
        if(set.size() == size){
            size++;
        }

        return set.add(el);
    }

    public boolean contains(int el){
        return set.contains(el);
    }

    public boolean del(int el){
        return set.remove(el);
    }

    public int getSize(){
        return size;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();

        for (int el :
                set) {
            str.append(el).append("-");
        }

        return str.substring(0, str.lastIndexOf("-"));
    }
}
