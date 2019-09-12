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
