/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package toko.toko.ws.b;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author user
 */
@Controller
@ResponseBody
public class TokoController {
    
    Barang data = new Barang();
    BarangJpaController bctrl = new BarangJpaController();
    
    //List<Barang> lsBarang = new arrayList();
    
    //fungsi get untuk menampilkan data barang
    @RequestMapping("/getBarang/{id}")
    public String getBarang(@PathVariable("id") int id){
        try{
            data = bctrl.findBarang(id);
            return data.getNama()+ " " + data.getJumlah().toString();
        }
        catch (Exception error){
            return "Data tidak ada";
        }
        
    }
    
    //menampilkan semua data
    @RequestMapping("/getall")
    public List<Barang> viewAll()
    {
        return bctrl.findBarangEntities();
    }
    
    //fungsi delete untuk menghapus data barang
     @RequestMapping("/delete/{id}")
    public String deleteById(@PathVariable("id") int id){
        try{
        bctrl.destroy(id);
        return id + "deleted";
        }
        catch (Exception error){
            return "error";
        }
    }
    
    //create data
    @RequestMapping(value="/create", method = RequestMethod.POST)
    public String createData(@RequestBody Barang barang){
        try{
            bctrl.create(barang);
            return "Data berhasil ditambahkan";
        } catch(Exception e){
            return "Data gagal ditambahkan";
        }
    } 
    
    //update data
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public String editData(@RequestBody Barang barang, @PathVariable("id") String id){
        try{
            data.setId(Integer.parseInt(id));
            data.setNama(barang.getNama());
            data.setJumlah(Integer.parseInt(barang.getJumlah().toString()));
            bctrl.edit(data);
            return "Data berhasil di ubah";
        } catch (Exception e){
            return "Data gagagl di ubah";
        }
    }
    
}
