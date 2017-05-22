require('dotenv').config()
const db = require('./db')()

db.models.User.create({
  id: 'user_1',
  name: 'John Doe',
  email: 'johndoe@email.com'
}).then(user => {
  return db.models.Comment.create({
    id: 'comment_1',
    text: 'Hello world',
    userId: user.id
  }).then(comment => {
    return comment.setUser(user)
  })
}).then(comment => {
  console.log('created comment');
})


// sql.Comment.create({
//   id: 'comment_2',
//   text: 'Hello world 2',
//   userId: 'user_1'
// }).then(comment => {
//   console.log('comment created');
// })

// sql.Comment.create({
//   id: 'comment_3',
//   text: 'Hello world 3'
// }).then(comment => {
//   console.log('comment created');
// }).catch(err => {
//   console.log(err);
// })
