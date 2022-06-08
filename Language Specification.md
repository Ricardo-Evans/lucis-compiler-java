# Lucis Language Specification

## Format of Binary Module

(All strings are encoded in UTF-8 without specifications.)

(All data size is counted as bytes without specifications.)

|         field         |      type      |               description               |
|:---------------------:|:--------------:|:---------------------------------------:|
|         magic         |     bytes      |                 4 bytes                 |
|      name_length      |    integer     |            extendable number            |
|         name          |     bytes      |               name_length               |
|        version        |    integer     |            extendable number            |
|   dependency_count    |    integer     |            extendable number            |
|     dependencies      |  string array  |  consists of dependency_count strings   |
|    constant_count     |    integer     |            extendable number            |
|     constant_pool     | constant array |  consists of constant_count constants   |
|     symbols_count     |    integer     |            extendable number            |
|     symbols_table     |  symbol array  |    consists of symbol_count symbols     |
| initialize_code_count |    integer     |            extendable number            |
|   initialize_codes    |   bytecodes    | consists of initialize_code_count codes |

## Format of Extendable Number

For each byte, only lower 7-bit is regard as number, the highest 1 bit indicates whether next byte should be considered in this number.

For example: 0b10000010 00000001 is actually 0b100000001.

## Format of Constant

| field | size(bytes)  |
|:-----:|:------------:|
| flag  |      1       |
| data  | undetermined |

### Flag of Constant

the higher 4 bits is used to determine the type of the constant

the lower 4 bits is used to store additional information various for each type

**Table of higher half flags**

|   name    | value |
|:---------:|:-----:|
|  Integer  | 0x1_  |
|  Decimal  | 0x2_  |
|  String   | 0x3_  |
| Function  | 0x4_  |
|   Type    | 0x5_  |
|   Kind    | 0x6_  |
| Signature | 0x7_  |
|   Tuple   | 0x8_  |

### Format of Integers/Decimals

assuming the lower half of the constant flag is n:

if n is not zero, $2^{n-1}$ is the size of this constant in bytes, and the constant data contains $2^{n-1}$ bytes

if n is zero, the size of this constant is not determined, and the constant data need to be parsed as an extendable number

### Format of Strings

The lower half of the constant flag is reserved and should be set to 0

The constant data contains an extendable number n, followed n bytes represent the UTF-8 encoding of the string

### Format of Functions

**Constant Flag(Lower Half)**

|  field   | size(bits) |
|:--------:|:----------:|
| dynamic  |     1      |
| override |     1      |
|  final   |     1      |
|  native  |     1      |

**The Constant Data Field Format**

|         field         |     exist condition     |   type    |               description               |
|:---------------------:|:-----------------------:|:---------:|:---------------------------------------:|
|     function_name     |         always          |  string   |   extendable pointer to constant pool   |
|    parameter_types    |         always          |   tuple   |   extendable pointer to constant pool   |
|     result_types      |         always          |   tuple   |   extendable pointer to constant pool   |
|   override_function   |  override flag is set   | function  |   extendable pointer to constant pool   |
|  dynamic_parameters   |   dynamic flag is set   |   tuple   |   extendable pointer to constant pool   |
|      stack_size       | native flag is not set  |  integer  |            extendable number            |
| function_content_size | native flag is not set  |  integer  |               extendable                |
|   function_content    | native flag is not set  | bytecodes | consists of function_content_size codes |

### Format of Types

**Constant Flag(Lower Half)**

|    field    | size(bit) |
|:-----------:|:---------:|
|    class    |     1     |
|    trait    |     1     |
| kind_result |     1     |
|    stub     |     1     |

#### Class Types
#### Trait Types
#### Kind Result Types
#### Stub Types

Stub types are only used in constraints of traits. When a class declares it has a trait, all stub types in the corresponding constraints should be replaced with the class type.

|   field   |  type   |             description             |
|:---------:|:-------:|:-----------------------------------:|
|  method   |  byte   |      how to process stub type       |
| reference | integer | extendable pointer to constant pool |

### Format of a Symbol

| name  |                size                 |
|:-----:|:-----------------------------------:|
| name  | extendable pointer to constant pool |
| type  | extendable pointer to constant pool |
| value | extendable pointer to constant pool |

Note that the type field may point to a type constant, or a string constant representing the type name.

### Table of Bytecode

