import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.* ;
public class Inventory {
    private final HashMap<String , Item> itemMap = new HashMap<>() ; 
    private final HashMap<String , Integer> stockMap = new HashMap<>() ; 
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void addItem(Item item , int qt){
        rwLock.writeLock().lock();
        try{
            itemMap.put(item.getCode() , item) ; 
            stockMap.merge(item.getCode() , qt,Integer::sum) ; 

        }
        finally{
            rwLock.writeLock().unlock();
        }
    }

    public void restock(String code , int qt){
        rwLock.writeLock().lock(); 
        try{
            if(!itemMap.containsKey(code)){
                throw new InvalidItemCodeExceptions("No suc item :" + code) ; 
            }

            stockMap.merge(code,qt,Integer::sum) ; 
        }
        finally{
            rwLock.writeLock().unlock();
        }

    }

    public void reduceStock(String code){
        rwLock.writeLock().lock();
        try{
            Integer qty = stockMap.get(code) ; 
            if(qty==null ||qty<=0){
                throw new OutOfStockExceptions("Item is out of stock" +code) ; 

            }
            stockMap.put(code , qty-1); 
        }
        finally{
            rwLock.writeLock().unlock();
        }
    }
    
    public Item getItem(String code){
        rwLock.readLock().lock();
        try{
            Item item = itemMap.get(code) ; 
            if(item==null){
                throw new InvalidItemCodeExceptions("Item code is invalid" + code);

            }
            return item ; 
        }
        finally{
            rwLock.readLock().unlock();
        }
    }

    public boolean isAvailable(String code){
        rwLock.readLock().lock();
        try{
            Integer qty = stockMap.get(code) ; 
            return qty!=null && qty>0 ; 
        }
        finally{
            rwLock.readLock().unlock();
        }
    }

    public Map<String,Integer> getStockSnapshot(){
        rwLock.readLock().lock();
        try{
            return new HashMap<>(stockMap) ; 
        }
        finally{
            rwLock.readLock().unlock();
        }

        
    }

    public Collection<Item> getAllItems() {
        rwLock.readLock().lock();
        try {
            return new ArrayList<>(itemMap.values());
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
