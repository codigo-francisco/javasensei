db.system.js.save(
   {
     _id: "ultimoIdAlumno",
     value : function() { return db.alumnos.count(); }
   }
);

db.loadServerScripts();