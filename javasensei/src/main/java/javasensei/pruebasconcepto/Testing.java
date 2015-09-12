package javasensei.pruebasconcepto;


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
        for(int i = 0; i <= 4; i++) {
            switch(i) {
                case 0: System.out.print("A");
                case 1: System.out.print("B");
                case 2: System.out.print("C");
            }
        }
 



    }
}
