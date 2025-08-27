---
config:
  layout: elk
---
classDiagram
    direction LR
    class UserRole {
        <<Enum>>
        +ADMIN
        +WAITER
        +COOK
    }
    class TableStatus {
        <<Enum>>
        +AVAILABLE
        +OCCUPIED
    }
    class OrderStatus {
        <<Enum>>
        +OPEN
        +CLOSED
        +PAID
    }
    class OrderItemStatus {
        <<Enum>>
        +PENDING
        +PREPARING
        +READY
        +DELIVERED
    }
    class User {
        <<Entity>>
        -id: UUID
        -fullName: String
        -email: String
        -passwordHash: String
        -role: UserRole
    }
    class RestaurantTable {
        <<Entity>>
        -id: UUID
        -tableNumber: int
        -status: TableStatus
    }
    class Category {
        <<Entity>>
        -id: UUID
        -name: String
    }
    class MenuItem {
        <<Entity>>
        -id: UUID
        -name: String
        -description: String
        -price: BigDecimal
        -isAvailable: boolean
    }
    class Order {
        <<Entity>>
        -id: UUID
        -status: OrderStatus
        -totalAmount: BigDecimal
        -createdAt: Timestamp
        -closedAt: Timestamp
    }
    class OrderItem {
        <<Entity>>
        -quantity: int
        -priceAtOrder: BigDecimal
        -status: OrderItemStatus
    }
    class UserResponseDTO { <<DTO>>; +id: UUID; +fullName: String; +email: String; +role: String }
    class CreateUserRequestDTO { <<DTO>>; +fullName: String; +email: String; +password: String; +role: String }
    class UpdateUserRequestDTO { <<DTO>>; +fullName: String; +email: String; +password: String }
    class CategoryResponseDTO { <<DTO>>; +id: UUID; +name: String }
    class MenuItemResponseDTO { <<DTO>>; +id: UUID; +name: String; +description: String; +price: BigDecimal; +isAvailable: boolean; +category: CategoryResponseDTO }
    class CreateMenuItemRequestDTO { <<DTO>>; +name: String; +description: String; +price: BigDecimal; +categoryId: UUID }
    class UpdateMenuItemRequestDTO { <<DTO>>; +name: String; +description: String; +price: BigDecimal; +isAvailable: boolean; +categoryId: UUID }
    class OrderItemResponseDTO { <<DTO>>; +menuItemName: String; +quantity: int; +price: BigDecimal; +status: String }
    class OrderResponseDTO { <<DTO>>; +id: UUID; +tableNumber: int; +waiterName: String; +status: String; +totalAmount: BigDecimal; +createdAt: Timestamp; +items: List~OrderItemResponseDTO~ }
    class CreateOrderItemDTO { <<DTO>>; +menuItemId: UUID; +quantity: int }
    class CreateOrderRequestDTO { <<DTO>>; +tableId: UUID; +waiterId: UUID; +items: List~CreateOrderItemDTO~ }
    class UpdateOrderStatusRequestDTO { <<DTO>>; +status: String }
    class MenuItemController {
        <<Controller>>
        +create(dto: CreateMenuItemRequestDTO): MenuItemResponseDTO
        +findById(id: UUID): MenuItemResponseDTO
        +findAll(): List~MenuItemResponseDTO~
        +update(id: UUID, dto: UpdateMenuItemRequestDTO): MenuItemResponseDTO
        +deleteById(id: UUID): void
    }
    class MenuItemService {
        <<Service>>
        +create(dto: CreateMenuItemRequestDTO): MenuItem
        +findById(id: UUID): MenuItem
        +findAll(): List~MenuItem~
        +update(id: UUID, dto: UpdateMenuItemRequestDTO): MenuItem
        +deleteById(id: UUID): void
    }
    class MenuItemRepository {
        <<Repository>>
        // extends JpaRepository<MenuItem, UUID>
    }
    class UserController {
        <<Controller>>
        +create(dto: CreateUserRequestDTO): UserResponseDTO
        +findById(id: UUID): UserResponseDTO
        +findAll(): List~UserResponseDTO~
        +update(id: UUID, dto: UpdateUserRequestDTO): UserResponseDTO
        +deleteById(id: UUID): void
    }
    class UserService {
        <<Service>>
        // Lógica de hash de senha acontece aqui
        +create(dto: CreateUserRequestDTO): User
        +findById(id: UUID): User
        +findAll(): List~User~
        +update(id: UUID, dto: UpdateUserRequestDTO): User
        +deleteById(id: UUID): void
    }
    class UserRepository {
        <<Repository>>
        // extends JpaRepository<User, UUID>
    }
    class OrderController {
        <<Controller>>
        +create(dto: CreateOrderRequestDTO): OrderResponseDTO
        +findById(id: UUID): OrderResponseDTO
        +findAll(): List~OrderResponseDTO~
        +updateStatus(id: UUID, dto: UpdateOrderStatusRequestDTO): OrderResponseDTO
    }
    class OrderService {
        <<Service>>
        // Lógica de cálculo de total, verificação de estoque, etc.
        +create(dto: CreateOrderRequestDTO): Order
        +findById(id: UUID): Order
        +findAll(): List~Order~
        +updateStatus(id: UUID, status: OrderStatus): Order
    }
    class OrderRepository {
        <<Repository>>
        // extends JpaRepository<Order, UUID>
    }
    Category "1" -- "0..*" MenuItem
    User "1" -- "0..*" Order
    RestaurantTable "1" -- "0..*" Order
    Order "1" *-- "1..*" OrderItem
    MenuItem "1" -- "0..*" OrderItem
    MenuItemController ..> MenuItemService
    MenuItemService ..> MenuItemRepository
    UserController ..> UserService
    UserService ..> UserRepository
    OrderController ..> OrderService
    OrderService ..> OrderRepository
    OrderService ..> MenuItemRepository
    OrderController ..> MenuItemResponseDTO
    OrderController ..> CreateMenuItemRequestDTO
    OrderController ..> UpdateMenuItemRequestDTO
    UserController ..> UserResponseDTO
    UserController ..> CreateUserRequestDTO
    UserController ..> UpdateUserRequestDTO
    OrderController ..> OrderResponseDTO
    OrderController ..> CreateOrderRequestDTO
    OrderController ..> UpdateOrderStatusRequestDTO
