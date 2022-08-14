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

For each byte, only lower 7-bit is regarded as number, the highest 1 bit indicates whether next byte should be considered
in this number.

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

if n is zero, the size of this constant is not determined, and the constant data need to be parsed as an extendable
number

### Format of Strings

The lower half of the constant flag is reserved and should be set to 0

The constant data contains an extendable number n, followed n bytes represent the UTF-8 encoding of the string

### Format of Functions

**Constant Flag(Lower Half)**

|    field    | value  |
|:-----------:|:------:|
|   dynamic   | 0b0001 |
|  override   | 0b0010 |
|   native    | 0b0100 |
|  abstract   | 0b1000 |
| kind_result | 0b1111 |

dynamic and override flags can be set with other flags

native and abstract flags can not be set at the same time

kind_result flag is a special case. if set, the actual flag is determined according to the prototype (see: kind - kind
result section)

**The Constant Data Field Format**

if kind_result flag is set, see kind -> kind result section; otherwise:

|         field         |     exist condition     |   type    |               description               |
|:---------------------:|:-----------------------:|:---------:|:---------------------------------------:|
|     function_name     |         always          |  string   |   extendable pointer to constant pool   |
|    parameter_types    |         always          |   tuple   |   extendable pointer to constant pool   |
|     result_types      |         always          |   tuple   |   extendable pointer to constant pool   |
|   override_function   |  override flag is set   | function  |   extendable pointer to constant pool   |
|  dynamic_parameters   |   dynamic flag is set   |   tuple   |   extendable pointer to constant pool   |
|      stack_size       | native flag is not set  |  integer  |            extendable number            |
| function_content_size | native flag is not set  |  integer  |            extendable number            |
|   function_content    | native flag is not set  | bytecodes | consists of function_content_size codes |

### Format of Types

**Constant Flag(Lower Half)**

|    field    | value  |
|:-----------:|:------:|
|    class    | 0b0001 |
|    trait    | 0b0010 |
| kind_result | 0b1111 |

#### Class Types

**The Constant Data Field Format**

|    field    |    type     |                         description                         |
|:-----------:|:-----------:|:-----------------------------------------------------------:|
|  type_name  |   string    |             extendable pointer to constant pool             |
|    base     |    type     |             extendable pointer to constant pool             |
| trait_count |   integer   |                      extendable number                      |
|   traits    | type array  | consists of trait_count extendable pointer to constant pool |
| field_count |   integer   |                      extendable number                      |
|   fields    | field array |               consists of field_count fields                |

**The Field Format**

| field |  type  |             description             |
|:-----:|:------:|:-----------------------------------:|
| name  | string | extendable pointer to constant pool |
| type  |  type  | extendable pointer to constant pool |

#### Trait Types

**The Constant Data Field Format**

|       field       |       type        |                              description                               |
|:-----------------:|:-----------------:|:----------------------------------------------------------------------:|
|     type_name     |      string       |                  extendable pointer to constant pool                   |
|    base_count     |      integer      |                           extendable number                            |
|       bases       |    type array     | consists of base_count extendable pointer to constant pool(base types) |
| requirement_count |      integer      |                           extendable number                            |
|   requirements    | requirement array |               consists of requirement_count requirements               |

**The Requirement Format**

|   field    |              type              |                description                |
|:----------:|:------------------------------:|:-----------------------------------------:|
| prototype  |       pointer(function)        |    extendable pointer to constant pool    |
| stub_count |            integer             |             extendable number             |
|   stubs    | integer array(0-based indexes) |         size equals to stub_count         |

#### Kind Result Types

see kind -> kind result section

### Format of a Symbol

| field |                size                 |
|:-----:|:-----------------------------------:|
| name  | extendable pointer to constant pool |
| type  | extendable pointer to constant pool |
| value | extendable pointer to constant pool |

### Table of Bytecode

