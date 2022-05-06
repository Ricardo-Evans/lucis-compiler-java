# Lucis Language Specification

### Format of binary module:

(All strings are encoded in UTF-8 without specifications.)

(All data size is counted as bytes without specifications.)

|         name          |                            size                            |
|:---------------------:|:----------------------------------------------------------:|
|         magic         |                             4                              |
|      name_length      |                         extendable                         |
|         name          |                        name_length                         |
|        version        |                         extendable                         |
|   dependency_count    |                         extendable                         |
|     dependencies      |   size undermined, consists of dependency_count strings    |
|    constant_count     |                         extendable                         |
|     constant_pool     |  size undetermined, consists of constant_count constants   |
|     symbols_count     |                         extendable                         |
|     symbols_table     |    size undetermined, consists of symbol_count symbols     |
| initialize_code_count |                         extendable                         |
|   initialize_codes    | size undetermined, consists of initialize_code_count codes |

### Format of extendable number:

For each byte, only lower 7-bit is regard as number, the highest 1 bit indicates whether next byte should be considered
in this number.

For example: 0b10000010 00000001 is actually 0b100000001.

### Format of a constant

|   name    |    size     |
|:---------:|:-----------:|
| type_flag |      1      |
| data_size | extendable  |
|   data    |  data_size  |

**Type Flag of Constant:**

|   name    | value |
|:---------:|:-----:|
|  Integer  | 0x01  |
|  Decimal  | 0x02  |
|  String   | 0x04  |
| Function  | 0x08  |
|   Type    | 0x10  |
|   Kind    | 0x20  |
| Signature | 0x40  |
|   Tuple   | 0x80  |

### Format of a symbol:

|   name    |                size                 |
|:---------:|:-----------------------------------:|
|   name    | extendable(point to constant pool)  |
|   type    | extendable(point to constant pool)  |
|   value   | extendable(point to constant pool)  |

Note that the type field may point to a type constant, or a string constant representing the type name.

### Table of Bytecode:

