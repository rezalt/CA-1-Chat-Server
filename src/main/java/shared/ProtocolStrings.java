package shared;

public class ProtocolStrings {
    
    public static enum ARGS{
        STOP,LOGIN,MSG,LOGOUT,CLIENTLIST,MSGRESP;
    }
    
    public String sendMessage(ARGS args, String msg){
        if(args.equals(ARGS.MSGRESP)){
            
        }
        return args+":"+msg;
    }
}