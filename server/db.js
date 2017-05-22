'use strict'

const Sequelize = require('sequelize')
let db = null

module.exports = done => {
  if (!db) {
    const sequelize = new Sequelize('sqlite:database', {
      storage: process.env.DB
    })
    db = {
      sequelize : sequelize,
      Sequelize : Sequelize ,
      models: {}
    }

    //Models
    db.models.User = require('./models/user')(sequelize, Sequelize)
    db.models.Comment = require('./models/comment')(sequelize, Sequelize)
    sequelize.sync()

    //Relations
    db.models.Comment.belongsTo(db.models.User, {
      foreignKey: 'userId',
      foreignKeyConstraint: true,
      allowNull: false
    })

    sequelize.sync().then(function(){
      done()
    })
  }
  return db
}
