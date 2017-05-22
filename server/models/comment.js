'use strict'

module.exports = (db, Datatype) => {
  const Comments = db.define('Comments', {
    id: {
      type: Datatype.STRING,
      allowNull: false,
      primaryKey: true
    },
    text: { type: Datatype.STRING },
    userId: {
      type: Datatype.STRING,
      allowNull: false
    }
  })
  return Comments
}
