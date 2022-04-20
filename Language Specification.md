# Lucis Language Specification

### Format of binary module:

(All strings are encoded in UTF-8 without specifications.)

(All data size is counted as bytes without specifications.)

|         name          |                       size                        |
|:---------------------:|:-------------------------------------------------:|
|         magic         |                         4                         |
|      name_length      |                      various                      |
|         name          |                    name_length                    |
|        version        |                      various                      |
|    constant_count     |                      various                      |
|     constant_pool     |    size undetermined, count of constant_count     |
|     symbols_count     |                      various                      |
|     symbols_table     |     size undetermined, count of symbol_count      |
| initialize_code_count |                      various                      |
|   initialize_codes    | size undetermined, count of initialize_code_count |

### Format of various sized number:

For each byte, only lower 7-bit is regard as number, the highest 1 bit indicates whether next byte should be considered in this number.

For example: 0b10000010 00000001 is actually 0b100000001.

### Format of a constant

|   name    |   size    |
|:---------:|:---------:|
| type_flag |     1     |
| data_size |  various  |
|   data    | data_size |

**Type Flag of Constant:**

|       name       | value |
|:----------------:|:-----:|
|     Integer      | 0x01  |
|     Decimal      | 0x02  |
|      String      | 0x04  |
|     Function     | 0x08  |
|    Class Type    | 0x10  |
|    Trait Type    | 0x20  |
|       Kind       | 0x40  |
| Dynamic Function | 0x80  |

### Format of a symbol:

|   name    |              size               |
|:---------:|:-------------------------------:|
|   name    | various(point to constant pool) |
|   type    | various(point to constant pool) |
|   value   | various(point to constant pool) |

Note that the type field may point to a type constant, or a string constant representing the type name.

### Table of Bytecode:

