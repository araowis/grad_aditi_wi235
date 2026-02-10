## Getting Started: Maintenance App

Welcome to the Maintenace App

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies
- `db`: the folder to maintain SQL files

Within `src`, we have:
- `context`: For application context (Admin or Owner)?
- `factory`: For login, Signup and service
- `model`: Contains model definitions
- `persistence`: Contains database operations and repositories
- `service`: Handles routing of control to relevant DAO operations
- `utils`: Extraneous functions like Password hashing and Date input 

PLEASE NOTE THAT `-g` FLAG IS NOT FUNCTIONAL AND IS MEANT FOR A USER LOOKING TO BUY A SITE

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Future goals:

### Short term (Known issues):

- One owner may own many sites (Done)
- Associate maintenance paid with sites (Done)
- Requests once approved should be deleted from maintenance request table (Done)
- Improve error handling (Primary key constraint, when thrown up, should be framed better by the program) (Pending)
- Multiple requests for maintenance payment approval for the same site should not be added to the maintenance table (Pending) (Currently, if the admin presses the relevant site ID all entries (even repeat ones) are removed so this has been lower priority)
- Refresh OWNER USER characteristic maintenance_paid IF AND ONLY IF all sites owned by OWNER USER are marked as maintenance_paid = true (Ongoing: Trigger functional. Logic to fire the trigger fails sometimes for unknown reasons --- diagnosis pending)

### Long term: 

- Separate business logic and data handling with SQL into separate functions: database should only be used to acquire and store data 
- Add functionality for `-g`: general user (User looking to buy a site)
- Add a MySQL or an Oracle database to check the extensibility of the project

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
