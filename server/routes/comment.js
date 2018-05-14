'use strict'

const router = require('express').Router({mergeParam: true})
const bodyParser = require('body-parser')
const db = require('../db')()
const Users = db.models.User
const Comments = db.models.Comment
const USER_FIELD = 'user'

router.use(bodyParser.json())

router.param('commentId', param)

router.route('/')
  .get(readAll)
  .post(create, alreadyExist)

router.route('/:commentId')
  .get(read)
  .put(update)
  .delete(deleteRoute)

function readAll(req, res, next){
  Comments.findAll({
    order: 'Comments.createdAt ASC',
    include: [{model: Users, as : USER_FIELD}]
  }).then(comments => {
    if(!comments){
      res.status(404).end()
      return
    }
    res.status(200)
    res.json(comments)
  }).catch(err => {
    console.log(err);
    res.status(500).json({status: 500, msg: 'internal server error'})
  })
}

function create(req, res, next){
  var userId = req.get('userId')
  if(!userId){
    res.status(401).json({status: 401, msg: 'unauthorized'})
    return
  }

  Users.findById(userId).then(user => {
    if(!user){
      res.status(401).json({status: 401, msg: 'unauthorized'})
      return
    }
    return Comments.create({
      id: req.body.id,
      text: req.body.text,
      userId: userId
    })
  }).then(comment => {
    res.status(201)
    res.json(comment)
  }).catch(err => {
    req.err = err
    next()
  })
}

function alreadyExist(req, res, next){
  if(req.err){
    var err = req.err
    if(err.errors && err.errors.length == 2){
      Comments.findById(req.body.id, {include: [{model: Users, as: USER_FIELD}]})
      .then(comment => {
        if(!comment){
          res.status(404).end()
          return
        }
        res.status(200)
        res.json(comment)
      })
      .catch(err => {
        console.log(err);
        res.status(500).json({status: 500, msg: 'internal server error'})
      })
      return
    }
  }
  res.status(500).json({status: 500, msg: 'internal server error'})
}

function param(req, res, next, id){
  Comments.findById(id, {include: [{model: Users, as: USER_FIELD}]}).then(comment => {
    if(!comment){
      res.status(404).end()
      return
    }
    req.comment = comment
    next()
  })
  .catch(err => {
    console.log(err);
    res.status(500).json({status: 500, msg: 'internal server error'})
  })
}

function read(req, res, next){
  res.status(200)
  res.json(req.comment)
}

function update(req, res, next){
  req.comment.text = req.body.text
  req.comment.save().then(comment => {
    res.status(200)
    res.json(comment)
  })
  .catch(next)
}

function deleteRoute(req, res, next){
  req.comment.destroy().then(comment => {
    res.status(204).end()
  })
  .catch(next)
}

module.exports = router
