package pruebitas;


public class Testing {
    public static void main(String[] args) {
        /*MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("prueba");
         
        DBCollection col = db.getCollection("Persons");
         
        DBObject doc1 = BasicDBObjectBuilder.start("name", "Pankaj").get();
        DBObject doc2 = BasicDBObjectBuilder.start("name", "Kumar").get();
        BulkWriteOperation operation = col.initializeOrderedBulkOperation();
        operation.insert(doc1);
        operation.insert(doc2);
        BulkWriteResult result = operation.execute();
        System.out.println("Documents inserted="+result.getInsertedCount()); // prints 2*/
         int x = 0, y = 0;

        System.out.print(x++);   
        System.out.print(++y);
        System.out.print(y--);   
        System.out.print(--x);
    }
}
