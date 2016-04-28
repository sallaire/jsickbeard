describe('angularjs homepage todo list', function() {
    var elements = {};


     it('should add a todo', function() {
       browser.get('https://angularjs.org');

       element(by.model('todoList.todoText')).sendKeys('write first protractor test');
       element(by.css('[value="add"]')).click();

       var todoList = element.all(by.repeater('todo in todoList.todos'));
       expect(todoList.count()).toEqual(3);
       expect(todoList.get(2).getText()).toEqual('write first protractor test');

       // You wrote your first test, cross it off the list
       todoList.get(2).element(by.css('input')).click();
       var completedAmount = element.all(by.css('.done-true'));
       expect(completedAmount.count()).toEqual(2);
     });

      it('should display home page', function() {
        init();
        expect(elements.home.titre.getText()).toEqual('Accueil');
       });

       var init = function () {
            browser.get('#');
            elements = {
               home : {
                titre : element(by.id('titre'))
               }
            };
       }

});
