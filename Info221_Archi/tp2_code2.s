# TP2 - Code2
	
.data
X:	.word 0xAABBCCDD
.text
.globl main

main:
	
    la $t0, X
    li $t5, 0x12345678
    lw $t5, 0($t0)
    lbu $t1, 0($t0)
    lbu $t2 1($t0)
    lbu $t3, 2($t0)
    lbu $t4, 3($t0)
    jr $31
