/*
 * Recursion.cpp
 *
 *  Created on: Sep 26, 2017
 *      Author: kumar
 */

#include <stdio.h>

#include <iostream>
#include <string>

void countDown(int n);
void countUp(int n);
void countUpAndDown(int n);
void countBetweenIncussive(int n1, int n2);
void countBetweenExclussive(int n1, int n2);
int power(int base, int exponent);
int powerEfficient(int base, int exponent);
void printString(std::string str);

int main()
{
	int n1 = 5, n2 = 10;
	int base = 2, exponent = 5;

	countUp(n1);
	printf("\n");
	countDown(n1);
	printf("\n");
	countUpAndDown(n1);
	printf("\n");
	countBetweenExclussive(n1, n2);
	printf("\n");
	countBetweenIncussive(n1, n2);
	printf("\n");
	printf("%d ** %d = %d\n", base, exponent, power(base, exponent));

	base = 2, exponent = 1;

	printf("\n");
	printf("%d ** %d = %d (efficient power) \n", base, exponent, powerEfficient(base, exponent));

	base = 2, exponent = 2;


	printf("\n");
	printf("%d ** %d = %d (efficient power)\n", base, exponent, powerEfficient(base, exponent));

	base = 2, exponent = 3;

	printf("\n");
	printf("%d ** %d = %d (efficient power)\n", base, exponent, powerEfficient(base, exponent));

	base = 2, exponent = 4;

	printf("\n");
	printf("%d ** %d = %d (efficient power)\n", base, exponent, powerEfficient(base, exponent));

	base = 2, exponent = 6;

	printf("\n");
	printf("%d ** %d = %d (efficient power)\n", base, exponent, powerEfficient(base, exponent));

	base = 2, exponent = 9;

	printf("\n");
	printf("%d ** %d = %d (efficient power)\n", base, exponent, powerEfficient(base, exponent));

	base = 2, exponent = 10;

	printf("\n");
	printf("%d ** %d = %d (efficient power)\n", base, exponent, powerEfficient(base, exponent));

	std::string str = "test string";
	printString(str);

}

void countDown(int n)
{
	if (n == 0) {
		return;
	}
	else {
		printf ("%d ", n);
		countDown(n - 1);
	}
}

void countUp(int n)
{
	if (n == 0) {
		return;
	}
	else {
		countUp(n - 1);
		printf ("%d ", n);
	}
}

void countBetweenIncussive(int n1, int n2)
{
	if (n1 == n2) {
		printf("%d ", n2);
	}
	else {
		printf ("%d ", n1);
		countBetweenIncussive(n1 + 1, n2);
	}
}

void countBetweenExclussive(int n1, int n2)
{
	if ((n2 - n1) <= 1) {
		return;
	}
	else {
		printf ("%d ", n1 + 1);
		countBetweenExclussive(n1 + 1, n2);
	}
}

void countUpAndDown(int n)
{
	if (n == 0) {
		return;
	}
	else {
		printf ("%d ", n);
		countUpAndDown(n - 1);
		printf ("%d ", n);
	}
}

int power(int base, int exponent)
{
	if (exponent == 0) {
		return 1;
	}
	else {
		return base * power (base, exponent - 1);
	}
}

int powerEfficient(int base, int exponent)
{
	int result;

	if (exponent == 0) {
		return 1;
	}
	else if (exponent == 1) {
		return base;
	}
	else if (exponent % 2 == 0) {
		result = base * powerEfficient(base, (exponent/2) - 1);
		return result * result;
	}
	else {
		result = base * powerEfficient(base, (exponent/2) - 1);
		return base * result * result;
	}
}

void printString(std::string str)
{
	if (str.length() == 0) {
		return;
	}
	else {
		std::cout << str.at(0);
		printString(str.substr(1));
	}
}
