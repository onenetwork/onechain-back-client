var bc = require('../backchain-client')
var expect = require('chai').expect
  , foo = 'bar'
  , beverages = { tea: [ 'chai', 'matcha', 'oolong' ] };
  

describe('Array', function() {
  describe('#indexOf()', function() {
    it('should return -1 when the value is not present', function() {
      expect(foo).to.equal('bar');
      console.info(bc("localhost:8545"))
    });
  });
});