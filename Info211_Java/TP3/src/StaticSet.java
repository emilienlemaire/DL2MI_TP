import java.util.HashSet;
import java.lang.Integer;

public class StaticSet {
    private int size;
    private HashSet<Integer> set;

    public StaticSet(int size){
        set = new HashSet<Integer>();
        this.size = size;
    }

    public boolean add(int el){
        if (set.size() == size) return false;
        set.add(el);
        return true;
    }

    public boolean contains(int el){
        return set.contains(el);
    }
    
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int el :
                set) {
            str.append(el).append("-");
        }

        return str.substring(0, str.lastIndexOf("-"));
    }
}
