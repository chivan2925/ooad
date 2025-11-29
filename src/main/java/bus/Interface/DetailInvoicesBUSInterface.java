package bus.Interface;

import java.util.List;

public interface DetailInvoicesBUSInterface<T, ID> {
    public void create(T detailInvoicesDTO, ID id);
    public void update(T detailInvoicesDTO, ID id);
    public void delete(ID invoiceID);
    public List<T> getById(ID invoiceID);
}
