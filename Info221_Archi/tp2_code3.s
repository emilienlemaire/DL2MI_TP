# TP2-Code 3
	
.data
X: .word -1 0 1 2 3 4 5 6 7 8
Y: .word 0 10 9 8 7 6 5 4 3 2
Z: .word 0 0 0 0 0 0 0 0 0 0
.text
.globl main

main:
	la $t0,X
  la $t1,Y
  la $t2,Z
  li $t3,1
  li $t4,10
debut_boucle:
  lw $t5,0($t0)
  lw $t6, 4($t1)
  add $t7,$t5,$t6
  sw $t7,4($t2)
  addiu $t0,$t0,4
  addiu $t1,$t1,4
  addiu $t2,$t2,4
  addiu $t3,$t3,1
  blt $t3,$t4, debut_boucle
  jr $ra
