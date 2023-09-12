
# Asset Tracer
## Built With

> - Spring Boot
> - Maven
> - Postgresql

## Getting Started
### Installation
> NOTE: 
<br>
> * JDK 17 required for all installation methods
<br>
> * Install Postgresql
<br>
> * Create database name asset_tracer
<br>
> * Run All SQl statement in script folder

## Usage
### Steps to test API

### [AuthController](http://localhost:8080/swagger-ui/index.html#/auth-controller/register)
```Create and login```
- Step 01 -> [Register](http://localhost:8080/swagger-ui/index.html#/auth-controller/register)
- Step 02 -> [Email Confirmation](http://localhost:8080/swagger-ui/index.html#/auth-controller/verifyEmail)
<br>
**Note:** get generated code from table email_verification 
- Step 03 -> [Login](http://localhost:8080/swagger-ui/index.html#/auth-controller/login)

```Forgot Password```
- Step 04 -> [Forgot Password](http://localhost:8080/swagger-ui/index.html#/auth-controller/forgotPassword)
<br>
**Note:** It will send confirmation link to your email then redirect to login page. But you should get generated code from table email_verification when you're working with swagger tool
- Step 05 -> [Check Valid Code](http://localhost:8080/swagger-ui/index.html#/auth-controller/resetPassword)
- Step 06 -> [Set New Password](http://localhost:8080/swagger-ui/index.html#/auth-controller/changPasswordAfterForgot)

```Other```
- Step 07 -> [Change Password](http://localhost:8080/swagger-ui/index.html#/auth-controller/changePassword)
- Step 08 -> [Update User](http://localhost:8080/swagger-ui/index.html#/auth-controller/updateUser)
- Step 09 -> [Get User](http://localhost:8080/swagger-ui/index.html#/auth-controller/getUserById)

### [RoleController](http://localhost:8080/swagger-ui/index.html#/role-controller/getAllRolesByFunc)

- Step 10 -> [Get All Roles](http://localhost:8080/swagger-ui/index.html#/role-controller/getAllRolesByFunc)
  <br>
  **Note:** Get all roles with pagination and search

- Step 11 -> [Get All Roles](http://localhost:8080/swagger-ui/index.html#/role-controller/getAllRoles)
  <br>
  **Note:** Get all roles without pagination and search

- Step 12 -> [Get Role ID](http://localhost:8080/swagger-ui/index.html#/role-controller/getRoleById)
- Step 13 -> [Create Role](http://localhost:8080/swagger-ui/index.html#/role-controller/addRole)
- Step 14 -> [Update Role](http://localhost:8080/swagger-ui/index.html#/role-controller/updateRole)

### [OrganizationController](http://localhost:8080/swagger-ui/index.html#/organization-controller/getAllOrganizations)
- Step 15 -> [Get All Organization](http://localhost:8080/swagger-ui/index.html#/organization-controller/getAllOrganizations)
  <br>
  **Note:** Get all Organization without pagination
- Step 16 -> [Get Organization By ID](http://localhost:8080/swagger-ui/index.html#/organization-controller/getOrganizationById)
- Step 17 -> [Create Organization](http://localhost:8080/swagger-ui/index.html#/organization-controller/addOrganization)
- Step 18 -> [Update Organization](http://localhost:8080/swagger-ui/index.html#/organization-controller/updateOrganization)
- Step 19 -> [Delete Organization](http://localhost:8080/swagger-ui/index.html#/organization-controller/deleteOrganization)
- Step 20 -> [Join Organization](http://localhost:8080/swagger-ui/index.html#/organization-controller/joinOrganization)
<br>
**Note:** Join organization with code and wait administrator approve
- Step 21 -> [Approve User Request Join](http://localhost:8080/swagger-ui/index.html#/organization-controller/approveUserRequestJoin)
- Step 22 -> [Get Organization By User ID](http://localhost:8080/swagger-ui/index.html#/organization-controller/getAllOrganizationByUserId)

### [RoomController](http://localhost:8080/swagger-ui/index.html#/room-controller)
- Step 23 -> [Get All Rooms](http://localhost:8080/swagger-ui/index.html#/room-controller/getAllRooms)
  <br>
  **Note:** Get all room with pagination, name and type of room
- Step 24 -> [Get Room By ID](http://localhost:8080/swagger-ui/index.html#/room-controller/getRoomById)
- Step 25 -> [Create Room](http://localhost:8080/swagger-ui/index.html#/room-controller/addRoom)
- Step 26 -> [Update Room](http://localhost:8080/swagger-ui/index.html#/room-controller/updateRoom)
- Step 27 -> [Delete Room](http://localhost:8080/swagger-ui/index.html#/room-controller/deleteRoom)

### [SuperCategoryController](http://localhost:8080/swagger-ui/index.html#/super-category-controller/getAllSuperCategories)
- Step 28 -> [Get All Super Category](http://localhost:8080/swagger-ui/index.html#/super-category-controller/getAllSuperCategories)
  <br>
  **Note:** Get all super category with pagination and name
- Step 29 -> [Get All Super Category Name](http://localhost:8080/swagger-ui/index.html#/super-category-controller/getAllSuperCategoryNames)
  <br>
  **Note:** Get all super category  without pagination
- Step 30 -> [Create Super Category](http://localhost:8080/swagger-ui/index.html#/super-category-controller/addSuperCategory)
- Step 31 -> [Update Super Category](http://localhost:8080/swagger-ui/index.html#/super-category-controller/updateSuperCategory)
- Step 32 -> [Delete Super Category](http://localhost:8080/swagger-ui/index.html#/super-category-controller/deleteSuperCategory)
- Step 33 -> [Get Super Category By ID](http://localhost:8080/swagger-ui/index.html#/super-category-controller/getSuperCategoryById)

### [NormalCategoryController](http://localhost:8080/swagger-ui/index.html#/normal-category-controller/getAllNormalCategories)
- Step 34 -> [Get All Normal Category](http://localhost:8080/swagger-ui/index.html#/normal-category-controller/getAllNormalCategories)
- Step 35 -> [Get Normal Category By ID](http://localhost:8080/swagger-ui/index.html#/normal-category-controller/getNormalCategoryById)
- Step 36 -> [Create Normal Category](http://localhost:8080/swagger-ui/index.html#/normal-category-controller/addNormalCategory)
- Step 37 -> [Update Normal Category](http://localhost:8080/swagger-ui/index.html#/normal-category-controller/updateNormalCategory)
- Step 38 -> [Delete Normal Category](http://localhost:8080/swagger-ui/index.html#/normal-category-controller/deleteNormalCategory)

### [InvoiceController](http://localhost:8080/swagger-ui/index.html#/invoice-controller/getAllInvoice)
- Step 39 -> [Get All Invoice With Pagination](http://localhost:8080/swagger-ui/index.html#/invoice-controller/getAllInvoice)
- Step 40 -> [Get All Invoice Without Pagination](http://localhost:8080/swagger-ui/index.html#/invoice-controller/getAllInvoices)
- Step 41 -> [Create Multiple Invoice](http://localhost:8080/swagger-ui/index.html#/invoice-controller/addInvoice)
- Step 42 -> [Create An Invoice](http://localhost:8080/swagger-ui/index.html#/invoice-controller/addInvoice2)
- Step 43 -> [Update Invoice](http://localhost:8080/swagger-ui/index.html#/invoice-controller/updateInvoice)
- Step 44 -> [Delete Invoice](http://localhost:8080/swagger-ui/index.html#/invoice-controller/deleteInvoiceById)
- Step 45 -> [Get Invoice By ID](http://localhost:8080/swagger-ui/index.html#/invoice-controller/getInvoiceById)

### [ItemDetailController](http://localhost:8080/swagger-ui/index.html#/item-detail-controller/getAllItemsDetail)
- Step 46 -> [Get All Item Detail ](http://localhost:8080/swagger-ui/index.html#/item-detail-controller/getAllItemsDetail)
- Step 47 -> [Get Item Detail By ID](http://localhost:8080/swagger-ui/index.html#/item-detail-controller/getItemDetailById)
- Step 48 -> [Create Item Detail](http://localhost:8080/swagger-ui/index.html#/item-detail-controller/addItemDetail)
- Step 49 -> [Update Item Detail](http://localhost:8080/swagger-ui/index.html#/item-detail-controller/updateItemDetail)
- Step 50 -> [Delete Item Detail](http://localhost:8080/swagger-ui/index.html#/item-detail-controller/deleteItemDetailById)

### [AssetController](http://localhost:8080/swagger-ui/index.html#/asset-controller/getAllAssetList)
- Step 51 -> [Get All Assets](http://localhost:8080/swagger-ui/index.html#/asset-controller/getAllAssets)
- Step 52 -> [Get All Assets With Filter](http://localhost:8080/swagger-ui/index.html#/asset-controller/getAllAssetList)
- Step 53 -> [Add Asset](http://localhost:8080/swagger-ui/index.html#/asset-controller/addAsset)
- Step 54 -> [Update Asset](http://localhost:8080/swagger-ui/index.html#/asset-controller/updateAssetById)
- Step 55 -> [Delete Asset](http://localhost:8080/swagger-ui/index.html#/asset-controller/deleteAsset)
- Step 56 -> [Get Asset By ID](http://localhost:8080/swagger-ui/index.html#/asset-controller/getAssetById)

### [OrganizationDetailController]()
- Step 57 -> [Get All Organization Detail By User ID](http://localhost:8080/swagger-ui/index.html#/organization-detail-controller/getAllOrganizationsDetailByUserId)
- Step 58 -> [Get Organization Detail By User ID and Organization ID](http://localhost:8080/swagger-ui/index.html#/organization-detail-controller/getOrganizationDetail)
