package broocebot;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Optional;

import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.MissingPermissionsException;
import sx.blah.discord.handle.EventDispatcher;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.MessageSendEvent;
import sx.blah.discord.handle.impl.events.GameChangeEvent;
import sx.blah.discord.handle.impl.events.MentionEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.UserVoiceStateUpdateEvent;
import sx.blah.discord.handle.impl.obj.Channel;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.AudioChannel;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.MessageBuilder;

public class Broocebot extends BaseBot{
	
	// Bot info
	String version = "0.1.0";
	String author = "Broocevelt";
	
	// Defaults
	Image defaultAvatar = Image.forFile(new File("resources/Broocebot.png"));
	String defaultName = "Broocebot";
	
	// Bot variables/references
	
	IVoiceChannel vchannel;
	AudioChannel channel;
	
	public Broocebot(IDiscordClient discordClient) {
		super(discordClient);
		EventDispatcher dispatcher = discordClient.getDispatcher(); //Gets the client's event dispatcher
		dispatcher.registerListener(this); //Registers the event listener
		System.out.println("Hello there");
	}
	
	// Channel: 126772873623896064
	// Guild: 124134288781344769
	
	@EventSubscriber
	public void handle(UserVoiceStateUpdateEvent e){
		
	}
	
	@EventSubscriber
	public void handle(MessageReceivedEvent event) throws MissingPermissionsException, HTTP429Exception, DiscordException{
		
		String oldmessage = event.getMessage().getContent();
		IUser user = event.getMessage().getAuthor();
		
		boolean isAdmin = false;
		if(user.getID().equals("124244483943432192"))
			isAdmin = true;
		
		if(oldmessage.startsWith("%")){
			
			String[] parts = (oldmessage.split(" ",2));
			String command = parts[0];
			String argumentChain = "";
			String[] arguments = null;
			
			if(parts.length == 2)
			{
				argumentChain = parts[1];
				arguments = argumentChain.split(" ");
			}
			
			Image img;
			
			switch(command){
				
				// Funcionalities
				
				case "%avatar":
					if(!isAdmin){
						String description = "Sólo Broocevelt puede ejecutar este comando";
						issueError(event, "Permiso", description);
						break;
					}
					if(arguments == null || arguments.length > 1){
						String description = "El formato correcto es\n%avatar [http://url.com/image.png | reset]";
						issueError(event, "Formato", description);
						break;
					}
					
					img = null;
					if(arguments[0].endsWith(".png"))
						img = Image.forUrl("png", arguments[0]);
					else
					{
						String description = "Sólo se admite formato .png";
						issueError(event, "Argumentos", description);
						break;
					}
					if(img != null)
						try{
							BaseBot.INSTANCE.client.changeAvatar(img);
						}
						catch(Exception e){
							String description = "La URL introducida no es válida";
							issueError(event,"URL", description);
							}
					break;
					
				case "%avatarreset":
					if(!isAdmin){
						String description = "Sólo Broocevelt puede ejecutar este comando";
						issueError(event, "Permiso", description);
						break;
					}
					if(arguments != null){
						String description = "El formato correcto es\n%avatarreset";
						issueError(event, "Formato", description);
						break;
					}
					
					img = defaultAvatar;
					
					if(img != null)
						try{
							BaseBot.INSTANCE.client.changeAvatar(img);
						}
						catch(Exception e){
							String description = "La URL interna no es válida";
							issueError(event,"URL", description);
							}
					break;
					
				case "%name":
					if(!isAdmin){
						String description = "Sólo Broocevelt puede ejecutar este comando";
						issueError(event, "Permiso", description);
						break;
					}
					if(arguments == null){
						String description = "El formato correcto es\n%name nombre";
						issueError(event, "Formato", description);
						break;
					}
					BaseBot.INSTANCE.client.changeUsername(parts[1]);
					break;
					
				case "%namereset":
					if(!isAdmin){
						String description = "Sólo Broocevelt puede ejecutar este comando";
						issueError(event, "Permiso", description);
						break;
					}
					if(arguments != null){
						String description = "El formato correcto es\n%namereset";
						issueError(event, "Formato", description);
						break;
					}
					BaseBot.INSTANCE.client.changeUsername(defaultName);
					break;
					
				case "%help":
					IPrivateChannel pc = null;
					pc = event.getClient().getOrCreatePMChannel(event.getMessage().getAuthor());
					
					pc.sendMessage("Saludos, soy el asistente **Broocebot "+version+"**\n"
							+ "Fui creado por el Sr. **"+author+"**\n\n"
							+ "Aquí tiene una lista de comandos que puedo ejecutar:\n"
							+"\n"
							+ "***Funcionalidades***\n"
							+"----------\n"
							+ "**%help** | *Obtiene la lista de comandos*\n"
							+ "**%avatar** enlace.png | *Cambia mi avatar*\n"
							+ "**%avatarreset** | *Restaura mi avatar original*\n"
							+ "**%name** nombre | *Cambia mi nombre*\n"
							+ "**%namereset** | *Restaura mi nombre original*\n"
							+"\n"
							+"***Transformarme***\n"
							+"----------\n"
							+ "**%whenyougetthosemeansofproductionjustright** | *Me convierto en alguien distinto*\n"
							+ "**%whenyougetthosegemsjustright** | *Me convierto en alguien distinto*\n"
							+"\n"
							+"***Comentarios***\n"
							+"----------\n"
							+ "**%praise** | *Alabado sea*\n"
							+ "**%haberlas** | *A ver, si haberlas haylas*\n"
							+ "**%furri** | *Mira no, ¿eh?*\n"
							+ "**%fua** | *De casa tarradellas*\n"
							+"\n"
							+"***Imágenes***\n"
							+"----------\n"
							+ "**%kiwi** | *La mejor de las frutas*\n"
							+ "**%ramen** | *The one true king*\n"
							+ "**%lift** | *La mejor filia*\n"
							+ "**%murcia** | *Fotografía de Cartagena*\n"
							+ "**%renfito** | *Su hábitat natural*\n"
							+ "**%ochinchin** | *Go daisuki nan da*\n"
							+ "**%papabless** | *\\*COUGH\\** EECH\n"
							+ "**%cyka** | *idi nahuy*\n"
							+ "**%acykar** | *idi nahuy?*\n"
							+ "**%dalas** | *The one true king*\n"
							+ "**%dross** | *Un ser espeluznante*\n"
							+"\n"
							+"***Videos***\n"
							+"----------\n"
							+ "**%stop** | *The time has come*\n"
							+"\n");
					break;
					
				// Avatar Changes
					
				case "%whenyougetthosemeansofproductionjustright":
					img = Image.forUrl("png", "http://i.imgur.com/ihO9RQI.png");
					BaseBot.INSTANCE.client.changeAvatar(img);
					break;
				case "%whenyougetthosegemsjustright":
					img = Image.forUrl("png", "http://i.imgur.com/PzXRx8y.png");
					BaseBot.INSTANCE.client.changeAvatar(img);
					break;
					
				// Text Commands
					
				case "%praise":
					event.getMessage().getChannel().sendMessage("Póstrate ante Broocevelt, <@"+user.getID()+">");
					break;
				case "%haberlas":
					event.getMessage().getChannel().sendMessage("¿Y las fantas para cuando?");
					break;
				case "%furri":
					event.getMessage().getChannel().sendMessage("Furri tu puta madre, ¿eh?");
					break;
				case "%fua":
					event.getMessage().getChannel().sendMessage("pab0");
					break;
				case "%tachanka":
					event.getMessage().getChannel().sendMessage("La tachanka (en ruso: тачанка) era un vehículo remolcado por caballos y armado con una ametralladora, generalmente una carreta o carro abierto con una ametralladora en su parte posterior. Una tachanka puede ser remolcada por dos o cuatro caballos y tiene una tripulación de dos o tres hombres (el cochero y los sirvientes de la ametralladora). Su invención se atribuye a Néstor Majnó.1 Fue utilizada durante la Guerra Civil Rusa, la Primera Guerra Mundial e incluso la Segunda Guerra Mundial.\nhttp://i.imgur.com/bU0vC1Y.jpg");
					break;
					
				// Picture Commands
					
				case "%lift":
					event.getMessage().getChannel().sendMessage("Do you even lift, bro? | http://i.imgur.com/oPYNyBk.gif");
					break;
				case "%ramen":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/vf57Cqh.jpg");
					break;
				case "%kiwi":
					event.getMessage().getChannel().sendMessage("¡Boingy~boingy~! | http://i.imgur.com/N063U5c.gif");
					break;
				case "%murcia":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/dU2MKu5.jpg");
					break;
				case "%renfito":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/Ylg2z12.png");
					break;
				case "%ochinchin":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/S4EMx10.png");
					break;
				case "%papabless":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/r6q6uXP.png");
					break;
				case "%cyka":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/0eRnwwg.gif");
					break;
				case "%acykar":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/VM5Hnth.png");
					break;
				case "%dalas":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/OuS6RHD.png");
					break;
				case "%dross":
					event.getMessage().getChannel().sendMessage("http://i.imgur.com/U1GhumR.png");
					break;
					
					
					
				// Video Commands
					
				case "%stop":
					event.getMessage().getChannel().sendMessage("It's time to stop | https://www.youtube.com/watch?v=2k0SmqbBIpQ");
					break;
				case "%roasted":
					event.getMessage().getChannel().sendMessage("My nigeria you just got roasted | https://www.youtube.com/watch?v=_tWC5qtfby4");
					break;
				case "%hilarious":
					event.getMessage().getChannel().sendMessage("Hilarious and Original | https://www.youtube.com/watch?v=-1yzoiUIGGs");
					break;
				case "%wakemeup":
					event.getMessage().getChannel().sendMessage("Wake me up | https://www.youtube.com/watch?v=gbt61vcAkG0");
					break;
					
				// Voice Commands
					
				case "%vjoin":

					vchannel = event.getClient().getGuildByID("124134288781344769")
							.getVoiceChannelByID("126772873623896064");
					channel = event.getClient().getAudioChannel();
					vchannel.join();
					File music = new File("resources/music/00/00.mp3");
					channel.queueFile(music);
					
					
					break;
				
				
			}
		}
	}
	
	public void issueError(MessageReceivedEvent event, String type, String description){
		try {
			event.getMessage().getChannel().sendMessage(
					"--------------------\n"
					+"__**ERROR**__\n**Tipo:** "+type+"\n**Descripción:**\n*"+description+"*\n"
					+"--------------------");
		} catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@EventSubscriber
	public void handle(GameChangeEvent event){
		String user = event.getUser().getName();
		String oldgame;
		String newgame;
		try{
			oldgame = event.getOldGame().get();
		}
		catch(NoSuchElementException e){oldgame = null;}
		try{
			newgame = event.getNewGame().get();
		}
		catch(NoSuchElementException e){newgame = null;}
		
		String msg;
		
		if(newgame == null)
			msg = "Te has desintoxicado de "+oldgame+", ¿eh "+user+"?";
		else if(oldgame != null)
			msg = "Que sepas "+user+" que "+oldgame+" era mejor";
		else
			msg = "Deja de jugar a "+newgame+" "+user+", la adicción es mala.";
		IChannel chan = (IChannel)client.getChannels(false).toArray()[0];
		try{
		chan.sendMessage(msg);
		}
		catch(Exception e){System.err.println(e);}
	}
	

	@EventSubscriber
	public void handle(ReadyEvent event) {
		try {
			BaseBot.INSTANCE.client.changeAvatar(defaultAvatar);
			BaseBot.INSTANCE.client.changeUsername(defaultName);
			Optional<String> game = Optional.of("%help for commands!");
			BaseBot.INSTANCE.client.updatePresence(false, game);
			IChannel chan = (IChannel)client.getChannels(false).toArray()[0];
			try {
				chan.sendMessage("Beep-boop, ¡listo para servir!");
			} catch (MissingPermissionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (DiscordException | HTTP429Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@EventSubscriber
	public void handle(MentionEvent event) {
		try{
			if(!event.getMessage().getAuthor().getID().equals("124244483943432192")){
				event.getMessage().getChannel().sendMessage("No lo intente, usted no me merece.");
			}
			else{
				event.getMessage().getChannel().sendMessage("¿Me ha invocado, Sr. <@"+event.getMessage().getAuthor().getID()+">?");
		}
		}
		catch(Exception e){System.err.println(e);}
	}
	
	
}
