'use strict'

const db = require('./db')(function(){
  console.log('DB initialized');
})
const express = require('express')
const morgan = require('morgan')
const usersRoute = require('./routes/user')
const commentsRoute = require('./routes/comment')

var app = express()

app.set('port', process.env.PORT || 9000)

app.use(morgan(':method :url :status :res[content-length] - :response-time ms'))

app.use('/users', usersRoute)
app.use('/comments', commentsRoute)

module.exports = app
