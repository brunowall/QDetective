package br.ufc.quixada.qdetective.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufc.quixada.qdetective.models.Denuncia;

/**
 * Created by darkbyte on 23/11/17.
 */

public class DenunciaServiceWeb {
    private ArrayList<Denuncia> denuncias;

    public boolean sendDenuncia(String url,Denuncia denuncia) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String json = gson.toJson(denuncia);
        URL api = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)api.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dos, "UTF-8"));
        writer.write(json.toString());
        writer.close();
        if(connection.getResponseCode()<HttpURLConnection.HTTP_BAD_REQUEST){
            return true;
        }else{
            return false;
        }
    }

    public boolean sendMidiaToserver(String url,File file) throws IOException {
        byte[]bytearray = loadFile(file);
        String encoded = Base64.encodeToString(bytearray,Base64.DEFAULT);
        HashMap<String, String> params = new HashMap<>();
        String nomearquivo = file.getName();
        if(file.getName().contains("/")){
            int index= nomearquivo.lastIndexOf("/");
            nomearquivo = nomearquivo.substring(index);
        }
        params.put("fileName", nomearquivo);
        params.put("base64", encoded);
        URL servidor = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) servidor.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "UTF-8");
        connection.setRequestProperty("Content-Length", Integer.toString(getPostDataString(params).length()));
        connection.setUseCaches(false);
        connection.connect();
        DataOutputStream dr = new DataOutputStream(connection.getOutputStream());
        dr.write(getPostDataString(params).getBytes());
        InputStream is = null;
        dr.flush();
        dr.close();
        String respostaServidor = null;
        int codigoResposta = connection.getResponseCode();
        if (codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST) {
            is = connection.getInputStream();
            respostaServidor = converterInputStreamToString(is);
        } else {
            is = connection.getErrorStream();
            respostaServidor = "Verifique a URL de conexão com o servidor. Erro: " + codigoResposta;
        }
        if(respostaServidor.contains("OK!")){
            return true;
        }else{
            return false;
        }

    }


    public String converterInputStreamToString(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        String linha = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((linha = br.readLine())!=null){
            sb.append(linha);
        }
        br.close();

        return sb.toString();
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        byte [] bin = new byte[(int) file.length()];

        int offset = 0;
        int numRead = 0;
        while (offset < bin.length && (numRead = is.read(bin, offset, bin.length - offset)) >= 0) {
            offset += numRead;
        }

        is.close();
        return bin;
    }

    public String downloadMidiaBase64(String url, String pathSalvamento, long id) {
        try {
            String imageBase64 = getJSONFromAPI(url + "/" + id);
            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap imagem = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            File file = new File(pathSalvamento);
            OutputStream fOut = new FileOutputStream(file);
            imagem.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.close();
        } catch (Exception fne) {
            fne.printStackTrace();

        }
        return null;
    }

    public List<Denuncia> getListaDenunciasJson(String url, String path, String id) {

        String uri = url + path + "/" + id;
        String json = getJSONFromAPI(uri);
        if (!json.isEmpty()) {
            try {
                GsonBuilder b = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter());
                Gson gson = b.create();
                Denuncia[] contato = gson.fromJson(json, Denuncia[].class);
                denuncias = new ArrayList<>(Arrays.asList(contato));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return denuncias;
    }

    private String getJSONFromAPI(String url) {
        String retorno = "";
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection conexao = (HttpURLConnection) apiUrl.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect();

            int codigoResposta = conexao.getResponseCode();
            InputStream is = null;
            if (codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = conexao.getInputStream();
                retorno = converterInputStreamToString(is);
            } else {
                is = conexao.getErrorStream();
            }
            is.close();
            conexao.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

}
