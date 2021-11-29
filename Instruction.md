### Instruction:
1. Launch the application on default IP and Port
2. Open folder as project in IntelijIDEA
3. Run TestCases.java as TestNG Test

### How to generate report:
1. As your tests completed go to Export Test Results
2. Choose format and folder

### Short Description of Automated Tests
Positive Tests:
1. AddUser - Trying to add new user and then to find them by name
2. DeleteUserWithContact - Trying to delete User who has Contacts in theirs PhoneBook

Negative Tests:
1. DeleteNotexistingContact - Create new User without Contacts and try to delete theirs Contact
2. InvalidEmailInContact - Create new User, then try to add a Contact with invalid email values 