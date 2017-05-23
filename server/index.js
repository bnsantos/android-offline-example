require('dotenv').config()

const app = require('./server')
app.listen(app.get('port'), function(){
  console.log('Started server');
})
