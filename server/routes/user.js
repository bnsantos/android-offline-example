'use strict'

const router = require('express').Router({mergeParam: true})
const bodyParser = require('body-parser')
const db = require('../db')()
const Users = db.models.User

router.use(bodyParser.json())

router.param('userId', param)

router.route('/').post(create)

router.route('/:userId')
  .get(read)
  .put(update)
  .delete(deleteRoute)

function param(req, res, next, id){
  Users.findById(id).then(user => {
    if(!user){
      res.status(404).end()
      return
    }
    req.user = user
    next()
  })
  .catch(err => {
    console.log(err);
    res.status(500).json({status: 500, msg: 'internal server error'})
  })
}

function create(req, res, next){
  Users.create(req.body)
    .then(user => {
      res.status(201)
      res.json(user)
    }).catch(next)
}

function read(req, res, next){
  res.status(200)
  res.json(req.user)
}

function update(req, res, next){
  req.user.name = req.body.name
  req.user.email = req.body.email
  req.user.save().then(user => {
    res.status(200)
    res.json(user)
  })
  .catch(next)
}

function deleteRoute(req, res, next){
  req.user.destroy().then(user => {
    res.status(204).end()
  })
  .catch(next)
}

module.exports = router
