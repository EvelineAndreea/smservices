package sm.utils.model;

public class Pair {
    private Element firstElement;
    private Element secondElement;

    public Pair(Element firstElement, Element secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public boolean contains(Element preference) {
        return firstElement.equals(preference) || secondElement.equals(preference);
    }

    public Element getFirst() {
        return firstElement;
    }

    public Element getSecond() {
        return secondElement;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (!(obj instanceof Pair)) return false;
        Pair pair = (Pair) obj;
        return (this.firstElement.equals(pair.getFirst())&& this.secondElement.equals(pair.getSecond()));
    }
}
