package mdcc.sd.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
 *  interface Calculadora que define os métodos remotos que a calculadora irá expor:
 */
public interface Calculadora extends Remote {
    int somar(int numeroA, int numeroB) throws RemoteException;
    int subtrair(int numeroA, int numeroB) throws RemoteException;
    int multiplicar(int numeroA, int numeroB) throws RemoteException;
    int dividir(int numeroA, int numeroB) throws RemoteException;
}