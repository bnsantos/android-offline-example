'use strict'

module.exports = (db, Datatype) => {
  const Users = db.define('Users', {
    id: {
      type: Datatype.STRING,
      allowNull: false,
      primaryKey: true
    },
    name: { type: Datatype.STRING },
    email: {
      type: Datatype.STRING,
      validate: { isEmail: true }
    }
  })
  return Users
}
