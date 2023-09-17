# Processor-Simulation
**Project Summary:**

This project focuses on simulating a fictional processor design and architecture in Java, with a choice of one of four processor packages. Package 4, known as the "Double Big Harvard Combo Large Arithmetic Shifts," serves as the basis for this simulation. The processor design comprises various components, including memory architecture, registers, an instruction set architecture, and a datapath with pipeline stages. Key features of this design include a Harvard architecture, separate instruction and data memory, 66 registers (including general-purpose registers and a status register), and a diverse instruction set with 12 opcodes.

The simulation entails parsing assembly language instructions from a text file, storing them in memory, and executing them following a pipelined approach. The pipeline consists of three stages: Instruction Fetch (IF), Instruction Decode (ID), and Execute (EX). Up to three instructions can run in parallel in the pipeline, and the execution follows a clock cycle-based model. The simulation should provide detailed outputs, including clock cycle numbers, pipeline stage updates, register and memory changes, and the final content of registers and memory.

The project's goal is to implement a processor simulation that accurately reflects the specified architecture and behavior, allowing for the execution of assembly language programs. 
