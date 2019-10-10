# TP2 - BubbleSort
	
.data
X:	.byte 5,1,4,2,8
.text
.globl main

main:
	
deb:	xor $t4, $t4, $t4	# swapped=false
	la $t3, X		# initialisation boucle
	addi $t5, $t3, 4
bcl:	?			#t1 = A[i-1]
	?			#t2 = A[i]
	slt $t6, $t2, $t1
	?			#pas d'échange à faire
	?			#A[i-1] <-> A[i]
	?			#A[i-1] <-> A[i]
	move $t4, $t6		#swapped=true
suite:	?			#i++
	bne $t3, $t5, bcl	# retour début boucle for
	?			# retour dévut boucle do-while 
	jr $ra




