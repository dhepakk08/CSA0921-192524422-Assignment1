package parking.model;
public interface Payable {
    long amountDue(long minutes);
    default String receipt(long minutes) { return "Amount due: Rs " + amountDue(minutes); }
    static long total(Payable[] items, long minutes) {
        long result = 0;
        for (Payable item : items) result = Math.addExact(result, item.amountDue(minutes));
        return result;
    }
}
