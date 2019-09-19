import numpy as np

np.array([1, 2])

# Exercice 2.3

print("Question 1 \n")

A, x = np.array([[1, 2, 3], [4, 5, 6]]), np.array([1, 2, 3])

print(A.shape, x.shape)
print(A.size, x.size)
print(A.ndim, x.ndim)
print(A.dtype, x.dtype)
print(A.sum(), x.sum())
print(A.prod(), x.prod())

print("\n Question2 \n")

nbColsA, nbLignesA = A.shape

print(nbColsA, nbLignesA)

prodLignesA = A.prod(axis=1)
prodColA = A.prod(axis=0)

print(prodLignesA, prodColA)

print("\nQuestion 3\n")

print(A*x)
# Multiplie chaque ligne de A par x (coefficient à coefficient)


print(A.dot(x))
# Fait la multiplication matricielle pour A et x

print(np.dot(A, x))
# Fait la multiplication matricielle pour A et x

# Exercice 2.3
print("\nExercice 2.3\n")
A, B, C, D = np.array([[7, 0], [-1, 5], [-1, 2]]), np.array([[1, 4], [-4, 0]]), np.array([7, 3]), np.array([8, 2])

print("A x B=", np.dot(A, B))
print("A x C=", np.dot(A, C))
print("C x D=", np.dot(C, D))
print("D x C=", np.dot(D, C))
print("D x B X C=", np.dot(np.dot(D, B), C))
print("AT x A=", np.dot(A.T, A))
print("A x AT=", np.dot(A, A.T))

# Exercice 2.4
print("\nExercice 2.4")

v1 = np.arange(1, 17, 1)
v2 = np.arange(0.0, 2.1, 0.2)
v3 = np.array([2**x for x in range(7)])

print(v1)
print(v2)
print(v3)

print("\nQuestion 2\n")
# Sûrement à refaire, en fonction du cours
A = np.array([[y**x for x in range(9)]for y in [2, 3, 5]])
B = np.array([np.arange(.0, 1.1, .2) for x in range(4)])
C = np.array([[y for x in range(7)] for y in [.0, .5, 1.]])

print(A)
print(B)
print(C)


print("\nQuestion 3\n")

A = np.array([[1 if (x == 0 or x == 9) else (1 if (y == 0 or y == 9) else (1 if x == y else 0)) for x in range(10)] for y in range(10)])

print(A)

print("\nQuestion 4\n")

L = np.array([[2 if i == j else (-1 if (i == j - 1 or i == j + 1) else 0) for j in range(10)] for i in range(10)])

print(L)

print("\nQuestion 5\n")

T = np.array([[1 if ((i + j) % 2 == 0) else 0 for j in range(10)] for i in range(10)])

print(T)
